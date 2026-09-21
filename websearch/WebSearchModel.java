import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Perform "web search" (from a  file), notify the interested observers of each query.
 */
public class WebSearchModel {
    private final File sourceFile;
    private final List<RegisteredQueryObserver> observers = new ArrayList<>();

    public interface QueryObserver {
        void onQuery(String query);
    }

    private static class RegisteredQueryObserver {
        private final QueryObserver observer;
        private final QueryFilter filter;

        private RegisteredQueryObserver(QueryObserver observer, QueryFilter filter) {
            this.observer = observer;
            this.filter = filter;
        }

        private boolean accepts(String query) {
            return filter.accept(query);
        }
    }

    public WebSearchModel(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void pretendToSearch() {
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
            while ( true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                notifyAllObservers(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addQueryObserver(QueryObserver queryObserver, QueryFilter queryFilter) {
        observers.add(new RegisteredQueryObserver(queryObserver, queryFilter));
    }

    private void notifyAllObservers(String line) {
        for (RegisteredQueryObserver registeredObserver : observers) {
            if (registeredObserver.accepts(line)) {
                registeredObserver.observer.onQuery(line);
            }
        }
    }
}
