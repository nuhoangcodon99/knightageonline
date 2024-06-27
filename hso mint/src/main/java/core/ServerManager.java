package core;

import Helps._Time;
import event.Event_1;
import event.NauKeo;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import client.Clan;
import client.Player;
import event_daily.ChiemThanhManager;
import event_daily.ChienTruong;
import event_daily.LoiDai;
import event_daily.LoiDaiManager;
import io.Session;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.Map;
import map.Mob_in_map;
import template.MainObject;

public class ServerManager implements Runnable {

    private static ServerManager instance;
    private final Thread mythread;
    private Thread server_live;
    private boolean running;
    private ServerSocket server;
    private final long time;
    public long time_l;
    private long time2;
    private byte checkError;

    public ServerManager() {
        this.time = System.currentTimeMillis();
        this.time_l = System.currentTimeMillis() + 60_000L;
        this.mythread = new Thread(this);
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    public void init() {
        Manager.gI().init();
        BossHDL.BossManager.init();
        ChiemThanhManager.init();
//        ChienTruongManager.initMap();
        server_update_right_time();
        this.running = true;
        this.mythread.start();
        this.server_live.start();
        new Thread(() -> {
            while (this.running) {
                if (this.time_l < System.currentTimeMillis()) {
//                    System.exit(0);
//Manager.gI().debug = true;
                }
                if (!server_live.isAlive() || !mythread.isAlive()) {
                    System.out.println("+++++++++++++Error alive-----------" + server_live.isAlive() + "   " + mythread.isAlive() + "   " + server.isClosed());
                }
                try {
                    Thread.sleep(5_000L);
                } catch (InterruptedException ex) {
                    System.out.println("core.ServerManager.init()");

                }
                if (System.currentTimeMillis() - time2 > 60_000 && this.running) {
                    try {
                        System.out.println("++++++++++++++++++++reset update+++++++++++++++");
                        time2 = System.currentTimeMillis();
                        if (server_live.isAlive()) {
                            server_live.interrupt();
                            server_update_right_time();
                            this.server_live.start();
                            File f = new File("ERROR/check.txt");
                            f.getParentFile().mkdirs();
                            if (!f.exists()) {
                                if (!f.createNewFile()) {
                                    System.out.println("Tạo file " + "ERROR/check.txt xảy ra lỗi");
                                    continue;
                                }
                            }
                            // Lưu mảng byte vào file
                            FileOutputStream fileOutputStream = new FileOutputStream("ERROR/check.txt");
                            fileOutputStream.write(("Lỗi ở đoạn : " + checkError).getBytes("utf-8"));
                            fileOutputStream.close();
                            System.out.println("Đã lưu mảng byte vào file ERROR/check.txt");
                        }
                    } catch (Exception eee) {
                    }
                }
            }
            System.out.println("-----------GAME EXIT 3----------");
        }).start();
    }

    public void running() {
        Calendar now;
        int hour, min, sec, millis, DayOfWeek;
        while (ServerManager.gI().running) {
            try {
                now = Calendar.getInstance();
                hour = now.get(Calendar.HOUR_OF_DAY);
                min = now.get(Calendar.MINUTE);
                sec = now.get(Calendar.SECOND);
                millis = now.get(Calendar.MILLISECOND);
                DayOfWeek = now.get(Calendar.DAY_OF_WEEK);
                if (min % 1 == 0 && sec == 10) { // update BXH + luu data
                    try {
                        SaveData.process();
                    } catch (Exception ee) {
                    }
                }
                long time_sleep = 1000 - millis;
                if (time_sleep > 0) {
                    Thread.sleep(time_sleep);
                }
            } catch (Exception e) {
            }
        }
    }

    public void run() {
        try {
            try {
                this.server = new ServerSocket(Manager.gI().server_port);
                System.out.println("Started in " + (System.currentTimeMillis() - this.time) + "s");
                System.out.println();
                System.out.println("LISTEN PORT " + Manager.gI().server_port + "...");
            } catch (Exception ee) {
                ee.printStackTrace();
                System.exit(0);
            }
            while (this.running) {
                try {
                    Socket client = this.server.accept();
                    if (this.running) {
                        Session ss = new Session(client);
                        ss.init();
                    }
                } catch (Exception eee) {
                    eee.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-----------GAME EXIT 2----------");
    }

    private void server_update_right_time() {
        this.server_live = new Thread(() -> {
            Calendar now;
            int hour, min, sec, millis, DayOfWeek;
            SaveData.process();
            while (this.running) {
                try {
                    time2 = System.currentTimeMillis();
                    checkError = 1;
                    now = Calendar.getInstance();
                    hour = now.get(Calendar.HOUR_OF_DAY);
                    min = now.get(Calendar.MINUTE);
                    sec = now.get(Calendar.SECOND);
                    millis = now.get(Calendar.MILLISECOND);
                    DayOfWeek = now.get(Calendar.DAY_OF_WEEK);
                    //
                    checkError = 2;
                    if (min % 5 == 0 && sec == 0) {
                        Manager.gI().chatKTGprocess("Bạn Đang Chơi Server" + " Hiệp Sĩ Shine " + "Chúc Bạn Chơi Game Vui Vẻ.");
                    }
                    checkError = 5;
                    if (min % 4 == 0 && sec == 0) {
                        Manager.gI().chatKTGprocess("Lưu Ý Không Để Cho Hành Trang Đầy Chừa 30 Ô Trong Hành Trang Để Tránh Gây Ra Lỗi Mất Đồ! Mua Túi Hành Trang Npc Lisa.");
                    }
                   //  checkError = 5;
                   // if (min % 2 == 0 && sec == 0) {
                     //   Manager.gI().chatKTGprocess("Code Test: 1-10.  Nguyên liệu Trắng Cấp 3 Mua Ở Npc Pháp Sư");
                  //  }
                    checkError = 3;
                    if (min % 11 == 0 && sec == 0) {
                        Manager.gI().chatKTGprocess("#Tips: Đến zulu để nhận điểm danh hàng ngày, Chơi vòng xoay, mở ly từ xa, hoặc thoát kẹt map hãy vào chức năng -> khác! ");
                        if (Manager.nameClanThue != null && !Manager.nameClanThue.isEmpty()) {
                            Manager.gI().chatKTGprocess("thuế hiện tại là " + Manager.thue + "% do bang " + Manager.nameClanThue + " đặt ra");
                        } else {
                            Manager.gI().chatKTGprocess("thuế hiện tại là " + Manager.thue + "% Chưa có bang nào giữ thuế");
                        }
                        CheckDDOS.ClearRam();
                    }
                    checkError = 4;
                    if (min % 1 == 0 && sec == 10) {
                        SessionManager.CheckBandWidth();
                    }
                    checkError = 5;
                    if (sec % 30 == 0) {
                        SessionManager.RemoveClient();
                    }
                    checkError = 6;
                    if (min % 5 == 0 && sec == 1) {
                        System.gc();
                    }
                    checkError = 7;
                    if (hour == 0 && min == 0 && sec == 1) {
                        Manager.gI().ip_create_char.clear();
                        for (Map[] map : Map.entrys) {
                            for (Map map0 : map) {
                                for (int i = 0; i < map0.players.size(); i++) {
                                    try {
                                        map0.players.get(i).change_new_date();
                                    } catch (Exception eee) {
                                    }
                                }
                            }
                        }
                    }
                    checkError = 8;

                    if (Manager.gI().event == 1) {
                        if (Event_1.naukeo == null) {
                            Event_1.naukeo = new NauKeo();
                        }
                        Event_1.naukeo.h = hour;
                        Event_1.naukeo.m = min;
                        if (hour == 17 && min == 00 && sec == 00) {
                            Event_1.list_nhankeo.clear();
                            Event_1.naukeo.start();
                            Manager.gI().chatKTGprocess("@Server: Bắt đầu nấu kẹo, các hiệp sĩ có thể tăng tốc thời gian nấu");
                        }
                        if (sec == 0 && min % 1 == 0) {
                            Event_1.naukeo.update(1);
                        }
                        if (min % 5 == 0 && sec == 0) {
                            Event_1.sort_bxh();
                        }
                    }
                    if (hour == 21 && min ==30 && sec == 0) {
                        ChienTruong.gI().open_register();
                         Manager.gI().chatKTGprocess("Chiến Trường Đã Bắt Đầu Nhanh Tay Lẹ Chân Lên");
                    }
                    if (sec % 1 == 0) {
                        LoiDaiManager.gI().Update();
                        ChienTruong.gI().update();
                    }
                    if(DayOfWeek % 2==0 && hour >= 20 && hour <= 23){
                        checkError = 16;
                        if(hour == 20 && min == 45)
                            ChiemThanhManager.StartRegister();
                        else if(hour == 21 && min == 30)
                            ChiemThanhManager.EndRegister();
                        checkError = 17;
                        ChiemThanhManager.update();
                    }
                    checkError = 9;
                    if (sec == 3 && min == 0 && (hour == 8 || hour == 20)) {
                        Manager.gI().chiem_mo.mo_open_atk();
                        Manager.gI().chatKTGprocess(" Thời gian chiếm mỏ đã đến!");
                    } else if (sec == 3 && min == 0 && (hour == 9 || hour == 21)) {
                        Manager.gI().chiem_mo.mo_close_atk();
                        Manager.gI().chatKTGprocess(" Thời gian chiếm mỏ đã đóng!");
                    }
                    checkError = 10;
                    if (hour == 18 && min == 0 && !LoiDaiManager.isRegister) {
                        LoiDaiManager.gI().startRegister();
                    }
                    checkError = 11;
                    if (min % 1 == 0 && sec == 4) {
                        Manager.gI().chiem_mo.harvest_all();
                    }
                    checkError = 12;
                    if (sec % 10 == 0) {
                        BossHDL.BossManager.Update();
                    }
                    checkError = 13;
                    if (Manager.gI().event == 2) {
                        ev_he.Event_2.Update();
                        if (min % 5 == 0 && sec == 0) {
                            ev_he.Event_2.sort_bxh();
                        }
                    }
                    if (Manager.gI().event == 3 && min % 5 == 0 && sec == 0) {
                        ev_he.Event_3.sort_bxh();
                    }
                    checkError = 14;
                    if (sec % 1 == 0) {
                        LoiDaiManager.gI().Update();
                    }
                    checkError = 15;

                    //
                    _Time.timeDay = _Time.GetTime();
                    checkError = 21;
                    long time_sleep = 1000 - millis;
                    if (time_sleep > 0) {
                        if (time_sleep < 100) {
                            System.err.println("server time update process is overloading...");
                        }
                        Thread.sleep(time_sleep);
                    }
                    checkError = 22;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("-----------GAME EXIT 1----------");
        });
    }

    public void close() throws IOException {
        System.out.println("----------SERVER CLOSE----------");
        running = false;
        server_live.interrupt();
        server.close();
//        instance = null;
    }
}
