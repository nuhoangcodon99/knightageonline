package core;

public class Start {

    public static void main(String[] args) {
//        for(int i = 0; i< 1000; i+= 10){
//            System.out.println("core.Start.main()   "+i+"  "+(i%40)+"   "+(i/40));
//        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                if (SQL.is_connected) {
                    SQL.gI().close();
                    System.out.println("SERVER STOPPED!");
                }
            }
        }));
        ServerManager.gI().init();
        
        ServerManager.gI().running();
    }
}
