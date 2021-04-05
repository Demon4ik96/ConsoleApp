import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {


        //Создаем возможность запускать приложение из командной строки
        Console console = System.console();

        if (console != null)
        {
            Scanner scanner = new Scanner(console.reader());
            System.out.println("Enter root path:");
            //Задаем корневой путь
            Path rootPath = Paths.get(scanner.nextLine());
        System.out.println("Enter port number:");
        //Задаем номер порта
        int portNumber = scanner.nextInt();
        //Запускаем в бесконечном цикле сервер, который слушает по заданному порту
        while (true)
            try (ServerSocket server = new ServerSocket(portNumber)) {
                //Ожидаем подключения клиентов
                Socket socket = server.accept();
                //В случае подключения клиента создаем и запускаем новый поток, чтобы клиенты работали параллельно
                new Thread(() -> {
                    try {
                        BufferedReader reader =
                                new BufferedReader(
                                        new InputStreamReader(socket.getInputStream()));

                        BufferedWriter writer =
                                new BufferedWriter(
                                        new OutputStreamWriter(socket.getOutputStream()));

                        writer.write("Enter mask");
                        writer.newLine();
                        writer.flush();
                        //Считываем маску от клиента
                        String mask = reader.readLine();
                        writer.write("Enter depth");
                        writer.newLine();
                        writer.flush();
                        //Считываем показатель глубины поиска от клиента
                        int depth = Integer.parseInt(reader.readLine());

                        //Создаем поток для поиска по дереву
                        SecondThread secondThread = new SecondThread(rootPath, mask, depth);
                        //Запускаем поток который производит поиск по дереву
                        secondThread.start();
                        //Выводим на экран клиента результаты поиска по дереву
                        while (true) {
                            if (!secondThread.x.isEmpty()) {
                                for (Path paths : secondThread.x) {
                                    writer.write(paths.toString());
                                    writer.newLine();
                                    writer.flush();
                                }
                                break;
                            } else continue;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
        else System.out.println("We do not have a console");

    }

}
