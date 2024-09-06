public class Game implements Runnable {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final Window window;
    private int ups;
    private int fps;
    private Thread thread;

    public Game() {
        this.window = new Window("Game", WIDTH, HEIGHT);

        this.ups = 0;
        this.fps = 0;
    }

    public void update() {
        System.out.println("Update");
        ups++;
    }

    public void draw() {
        System.out.println("Draw");
        fps++;
    }

    @Override
    public void run() {
        final int UPS = 300;
        final int FPS = 30;
        final double NS_PER_SECOND = 1_000_000_000;
        final double NS_PER_UPDATE = NS_PER_SECOND / UPS;
        final double NS_PER_FRAME = NS_PER_SECOND / FPS;

        // Guarda la ultima vez que se actualizo el juego
        long updateRef = System.nanoTime();
        // Guarda la ultima vez que se actualizo el frame
        long frameRef = System.nanoTime();
        // Guarda la ultima vez que se actualizo el contador
        long counterRef = System.nanoTime();

        while (true) {
            long currentTime = System.nanoTime();
            long updateElapsedTime = currentTime - updateRef;
            long frameElapsedTime = currentTime - frameRef;

            if (updateElapsedTime > NS_PER_UPDATE) {
                this.update();
                updateRef = currentTime;
            }

            if (frameElapsedTime > NS_PER_FRAME) {
                this.draw();
                frameRef = currentTime;
            }

            if (currentTime - counterRef > NS_PER_SECOND) {
                counterRef = currentTime;
                this.window.getFrame().setTitle("Game UPS: " + ups + " | FPS: " + fps);
                ups = 0;
                fps = 0;
            }
        }
    }

    public void start() {
        this.thread = new Thread(this, "GameThread");
        this.thread.start();
    }

    public void stop() {
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            // Aqui ocurre lo que pasa si el hilo no puede ser detenido
            e.printStackTrace();
        }
    }
}
