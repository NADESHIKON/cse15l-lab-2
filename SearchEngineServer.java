import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class SearchEngineURLHandler implements URLHandler {
    List<String> content = new ArrayList<>();
    private static Map<String, Function<URI, String>> HANDLERS = new HashMap<>();

    public SearchEngineURLHandler() {
        HANDLERS.put("/", url -> Arrays.toString(this.content.toArray()));
        HANDLERS.put("/search", url -> {
            String[] rawQuery = url.getQuery().split("=");
            String query = null;
            if (rawQuery.length == 2 && rawQuery[0].equalsIgnoreCase("query")) {
                query = rawQuery[1];
            }

            if (query == null) return "Please enter a valid search query!";

            String finalQuery = query;
            return "Result: " + this.content.stream().filter(c -> c.toLowerCase().contains(finalQuery.toLowerCase())).collect(Collectors.joining(", "));
        });
        HANDLERS.put("/add", url -> {
            String[] rawQuery = url.getQuery().split("=");
            String query = null;
            if (rawQuery.length == 2 && rawQuery[0].equalsIgnoreCase("content")) {
                query = rawQuery[1];
            }

            if (query == null) return "Please enter a valid add action!";

            String finalQuery = query;
            this.content.add(finalQuery);

            return "You have added " + finalQuery + " to the search engine!";
        });
    }

    @Override
    public String handleRequest(URI url) {
        String path = url.getPath();

        Function<URI, String> handler = HANDLERS.get(path.toLowerCase());
        if (handler != null) return handler.apply(url);

        return "404 Not Found!";
    }
}

class SearchEngineServer {
    public static void main(String[] args) throws IOException {
        int port = 8888;

        if(args.length != 0){
            port = Integer.parseInt(args[0]);
        }

        Server.start(port, new SearchEngineURLHandler());
    }
}