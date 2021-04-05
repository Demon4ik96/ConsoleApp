import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SecondThread extends Thread {

    private Path rootPath;
    private String mask;
    private int depth;
    public List<Path> x = new CopyOnWriteArrayList<>();

    SecondThread(Path rootPath,String mask,int depth)
    {
        this.depth=depth;
        this.rootPath=rootPath;
        this.mask=mask;
    }


    @Override
    public void run() {
        try {
                x = listFiles(rootPath, mask, depth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Path> listFiles(Path rootPath, String mask, int depth) throws IOException
    {
        List<Path> result;
        try(Stream<Path> walk = Files.walk(rootPath, depth)) {
            result=walk.filter(s->s.getFileName().toString().contains(mask)).collect(Collectors.toList());
        }
        return result;
    }
}
