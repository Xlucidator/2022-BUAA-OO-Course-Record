import java.util.ArrayList;

interface Observer {
    void update(String msg);
}

interface Observerable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObserver(String msg);
}

public class Task5 {
    public static void main(String[] args) {
        Server server = new Server(); //该类实现Observerable接口

        Observer user1 = new User("user1"); //该类实现Observer接口
        Observer user2 = new User("user2"); //该类实现Observer接口

        server.addObserver(user1);
        server.addObserver(user2);
        server.notifyObserver("北航的OO课是世界上最好的OO课！");

        server.removeObserver(user2);
        server.notifyObserver("Java是世界上最好用的语言。");
    }
}

class Server implements Observerable {
    private String name = "server";
    private ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObserver(String msg) {
        System.out.println(name + ": " + msg);
        observers.forEach(usr -> usr.update(msg));
    }
}

class User implements Observer {
    private String name;

    User(String name) {
        this.name = name;
    }

    public void update(String msg) {
        System.out.println(name + ": " + msg);
    }
}