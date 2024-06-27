package core;

import event.Event_1;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import client.Clan;
import client.Pet;
import client.Player;
import event_daily.ChiemThanhManager;
import event_daily.ChienTruong;
import event_daily.DailyQuest;

import event_daily.Group_ld;
import event_daily.LoiDai2;
//import event_daily.LoiDai;
import event_daily.LoiDaiManager;
import event_daily.MoLy;
import event_daily.Wedding;

import io.Message;
import io.Session;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map.Entry;
import map.Dungeon;
import map.Map;
import map.MapService;
import map.Mob_in_map;
import map.Vgo;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import template.EffTemplate;
import template.Item3;
import template.Item47;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.Level;
import template.Medal_Material;
import template.Member_ChienTruong;
import template.Option;
import template.Option_pet;
import template.Part_fashion;
import template.Pet_di_buon_manager;
import template.TaiXiuPlayer;
import template.box_item_template;

public class MenuController {

    public static void request_menu(Session conn, Message m) throws IOException {
        byte idnpc = m.reader().readByte();
        //System.out.println("core.MenuController.request_menu()"+idnpc);
        if (idnpc == -43 || idnpc == -45 || idnpc == -48 || idnpc == -46) {
            Menu_ChangeZone(conn);
            return;
        }
        // create menu per id npc
        String[] menu;
        switch (idnpc) {
            case -81: {
                menu = new String[]{"Đăng ký lôi đài", "Vào lôi đài", "Xem lôi đài", "Thông tin", "Xem điểm lôi đài", "Huong dan", "Tháo ngọc khảm"};
                break;
            }
            case -3: { // Lisa
                menu = new String[]{"Mua bán", "Mở ly", "Thuế", "Nhận quà chiến trường",
                    "Đóng"};
                break;
            }
            case -20: { // Lisa
                menu = new String[]{"Mua bán", "Mở ly", "Thuế", "Nhận quà chiến trường",
                    "Đóng"};
                break;
            }

            case -5: { // Hammer
                menu = new String[]{"Chiến Binh", "Sát Thủ", "Pháp Sư", "Xạ Thủ", "Chế tạo trang bị tinh tú", "Nâng cấp trang bị tinh tú", "Tháo Giáp Siêu Nhân ", "Tháo danh hiệu", "Chế Tạo Giáp Siêu Nhân "};
                break;
            }
            case -77: // Alisama
            case -22: { // Hammer
                menu = new String[]{"Chiến Binh", "Sát Thủ", "Pháp Sư", "Xạ Thủ"};
                break;
            }
            case -4: {// Doubar
                menu = new String[]{"Chiến Binh", "Sát Thủ", "Pháp Sư", "Xạ Thủ", "Thông Tin Boss"};
                break;
            }
            case -33: { // da dich chuyen
                menu = new String[]{"Thành Phố Cảng", "Thành Phố Kho Báu", "Khu Mua Bán", "Sa Mạc", "Vực Lún",
                    "Nghĩa Địa Cát", "Suối Ma", "Hầm Mộ Tầng 1", "Hầm Mộ Tầng 3", "Rừng Cao Nguyên", "Vách Đá Cheo Leo",
                    "Lối Lên Thượng Giới", "Đường Xuống Lòng Đất", "Cổng Vào Hạ Giới", "Khu Vườn"};
                break;
            }
            case -55: { // da dich chuyen
                menu = new String[]{"Thành Phố Cảng", "Khu Mua Bán", "Mê Cung", "Mê Cung Tầng 3", "Thị Trấn Mùa Đông",
                    "Thung lũng băng giá", "Chân núi tuyết", "Đèo băng giá", "Vực thẳm sương mù", "Trạm núi tuyết",
                    "Thành Phố Kho Báu"};
                break;
            }
            case -10: { // da dich chuyen
                menu = new String[]{"Làng Sói Trắng", "Thành Phố Kho Báu", "Khu Mua Bán", "Hang Lửa", "Rừng Ảo Giác",
                    "Thung Lũng Kỳ Bí", "Hồ Kí Ức", "Bờ Biển", "Vực Đá", "Rặng Đá Ngầm", "Đầm Lầy", "Đền Cổ", "Hang Dơi"};
                break;
            }
            case -8: {
                menu = new String[]{"Cửa Hàng Tóc", "Điểm danh hằng ngày", "Đổi coin sang ngọc", "Đổi coin sang vàng", (conn.p.type_exp == 0 ? "Bật" : "Tắt") + " nhận exp", "Đổi Mật Khẩu"};
//                if (conn.p.type_exp == 1) {
//                    menu = new String[]{"Cửa Hàng Tóc", "Điểm danh hằng ngày", "Đổi coin sang ngọc", "Đổi coin sang vàng",
//                        "Đăng ký treo chống pk", "Tgian còn lại", "Tắt nhận exp"};
//                } else {
//                    menu = new String[]{"Cửa Hàng Tóc", "Điểm danh hằng ngày", "Đổi coin sang ngọc", "Đổi coin sang vàng",
//                        "Đăng ký treo chống pk", "Tgian còn lại", "Bật nhận exp"};
//                }
                break;
            }
            case -36: {
                menu = new String[]{"Cường Hóa Trang Bị", "Shop Nguyên Liệu", "Chuyển hóa", "Hợp nguyên liệu mề đay",
                    "Mề đay chiến binh", "Mề đay pháp sư", "Mề đay sát thủ", "Mề đay xạ thủ", "Nâng cấp mề đay",
                    "Đổi dòng sát thương", "Đổi dòng % sát thương", "Hợp ngọc", "Khảm ngọc", "Đục lỗ"};
                break;
            }
            case -44: {
                menu = new String[]{"Nhận Quà  GiftCode", "Tháo cánh", "Tháo cải trang", "Tháo mề đay", "Tháo mặt nạ",
                    "Tháo cánh thời trang", "Tháo áo choàng", "Tháo tóc thời trang", "Tháo vũ khí thời trang",
                    "Tháo tai nghe thời trang", "Nhận đồ đã mua", "Nhận quà top nap + mốc"};
//                menu = new String[]{"Nhận GiftCode", "Tháo cánh", "Tháo cải trang", "Tháo mề đay", "Tháo mặt nạ",
//                    "Tháo cánh thời trang", "Tháo áo choàng", "Tháo tóc thời trang", "Tháo vũ khí thời trang",
//                    "Tháo tai nghe thời trang"};             
                break;
            }
            case -32: {
                menu = new String[]{"Xem BXH Level", "Xem BXH bang", "Đổi Áo Choàng"};
                break;
            }
            case -21: { // blackeye
                menu = new String[]{"Chiến Binh", "Sát Thủ", "Pháp Sư", "Xạ Thủ"};
                break;
            }
            case -90: { // keva
                menu = new String[]{"Shop", "Khu Phủ Sương Up", "Map Boss", "Quay Về Làng Phủ Sương", "Về Làng "};
                break;
            }
            case -7: {
                if (conn.user.contains("knightauto_hsr_")) {
                    menu = new String[]{"Rương giữ đồ", "Mở thêm ô hành trang", "Đăng ký tài khoản"};
                } else {
                    menu = new String[]{"Rương giữ đồ", "Mở thêm ô hành trang", "Mật khẩu rương"};
                }
                break;
            }
            case -34: { // cuop bien
                menu = new String[]{"Vòng xoay Vàng", "Vòng xoay ngọc", "Lịch sử","Tài Xĩu"};
                break;
            }
            case -2: { // zoro
                if (conn.p.myclan != null) {
                    if (conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
                        menu = new String[]{"Quản lý bang", "Shop Icon", "Shop Bang"};
                    } else {
                        menu = new String[]{"Kho Bang", "Góp Vàng", "Góp Ngọc", "Rời bang"};
                    }
                } else {
                    menu = new String[]{"Đăng ký bang", "Thông tin"};
                }
                break;
            }
            case -19: { // 
                if (conn.p.myclan != null) {
                    if (conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
                        menu = new String[]{"Quản lý bang", "Shop Icon", "Shop Bang"};
                    } else {
                        menu = new String[]{"Kho Bang", "Góp Vàng", "Góp Ngọc", "Rời bang"};
                    }
                } else {
                    menu = new String[]{"Đăng ký bang", "Thông tin"};
                }
                break;
            }
            case -85: { // mr edgar
                menu = new String[]{"Báo Thù", "Thông tin"};
                break;
            }
            case -42: { // pet
                menu = new String[]{"Chuồng thú", "Shop thức ăn", "Shop trứng", "Tháo pet"};
                break;
            }
            case -37: {
                menu = new String[]{"Vào Ngã Tư Tử Thần", "Giới thiệu", "BXH phó bản", "Đăng ký chiếm thành", "Vào chiếm thành",
                    "Xem Điểm Hiện Tại", "Nhận phần thưởng", "Trở thành hiệp sĩ"};
                break;
            }
            case -38:
            case -40: {
//                if (conn.p.dungeon != null && conn.p.dungeon.getWave() == 20) {
//                    menu = new String[]{"Tiếp tục chinh phục", "Bỏ cuộc", "Hướng dẫn"};
//                } else {
                Service.send_notice_box(conn, "Chưa có chức năng :(.");
                return;

            }
            case -41: {
                menu = new String[]{"Tạo cánh", "Nâng cấp cánh", "Kích hoạt cánh", "Tách cánh"};
                break;
            }
            case -49: { //menu_top
                menu = new String[]{"LIKE", "Shop Coin", "Kết Hôn", "Thông Tin Cá Nhân ", "Nhận quà hiếu chiến", "Làng Phủ Sương", "Khu Boss "};
                break;
            }
            case -82: {
                if (conn.p.map.ld2 != null) {
                    LoiDaiManager.gI().GetMenuViews(conn, conn.p.map.ld2.idxGroup);
                }
                return;
            }
//            case -81: {
//                menu = new String[]{"Điểm Pk"};
//                break;
//            }
            case -69: {
                if (Manager.gI().event == 1) {
                    menu = new String[]{"Đổi hộp đồ chơi", "Hướng dẫn", "Đăng ký nấu kẹo", "Bỏ nguyên liệu vào nồi kẹo",
                        "Lấy kẹo đã nấu", "Đổi túi kẹo", "Đổi trứng phượng hoàng băng", "Đổi trứng yêu tinh",
                        "Đổi giày băng giá", "Đổi mặt nạ băng giá", "Đổi kẹo gậy", "Đổi gậy tuyết", "Đổi xe trượt tuyết",
                        "Đổi trứng khỉ nâu"};

                } else if (Manager.gI().event == 2) {
                    menu = new String[]{"Mâm trái cây", "Top sự kiện", "Đổi quà may mắn"};
                    send_menu_select(conn, -69, menu, (byte) Manager.gI().event);
                    return;
                    //menu = new String[]{"Coming soon", infoServer.Website};
                } else if (Manager.gI().event == 3) {
                    menu = new String[]{"Đổi bó sen trắng", "Đổi hoa sen hồng", "Đổi bó sen hồng", "Xem top", "Đổi con lân", "Đổi trứng khỉ nâu", "Đổi trứng tiểu yêu", "Đổi cánh thời trang"};
                    send_menu_select(conn, -69, menu, (byte) Manager.gI().event);
                    return;
                    //menu = new String[]{"Coming soon", infoServer.Website};

                } else {
                    Service.send_notice_box(conn, "Chưa có chức năng :(.");
                    return;
                    //menu = new String[]{"Coming soon", infoServer.Website};
                }
                break;
            }
            case -62: {
                if (Manager.gI().event == 1) {
                    menu = new String[]{"Tăng tốc nấu", "Hướng dẫn", "Thông tin", "Top Nguyên Liệu"};
                } else {
                    Service.send_notice_box(conn, "Chưa có chức năng :(.");
                    return;
                    //menu = new String[]{"Coming soon", infoServer.Website};
                }
                break;
            }
            case -66: {
                if (Manager.gI().event == 1) {
             //       menu = new String[]{"Hoa tuyết", "Ngôi sao", "Quả châu", "Thiệp", "Top trang trí cây thông"};
                 } else {
              //      Service.send_notice_box(conn, "Chưa có chức năng :(.");
              //      return;
                }
                menu = new String[]{"Coming soon", infoServer.Website};
                break;
            }
            case -57: {
                menu = new String[]{"Mua bán"};
                break;
            }
            case -54: {
                menu = new String[]{"Đến Thành Phó Kho Báu"};
                break;
            }
            case -58: {
                menu = new String[]{"Mua lạc đà", "Bán đá quý", "Đồ thương nhân"};
                break;
            }
            case -59: {
                menu = new String[]{"Mua lạc đà", "Bán đá quý", "Đồ cướp"};
                break;
            }
            case -53: {
                menu = new String[]{" Đăng Ký Chiến trường", "Hướng dẫn", "Đổi đại bàng", "Vào Chiến Trường"};
                break;
            }
            default: {
                //System.out.println("core.MenuController.request_menu()"+idnpc);
                Service.send_notice_box(conn, "Chưa có chức năng :(.");
                return;
                //menu = new String[]{"Coming soon", infoServer.Website};
                //break;
            }
        }
        //
        send_menu_select(conn, idnpc, menu);
    }

    public static void processmenu(Session conn, Message m) throws IOException {
        short idnpc = m.reader().readShort();
        @SuppressWarnings("unused")
        byte idmenu = m.reader().readByte();
        byte index = m.reader().readByte();
//        System.out.println("core.MenuController.processmenu() npc: "+idnpc);
//        System.out.println("core.MenuController.processmenu() id: "+idmenu);
//        System.out.println("core.MenuController.processmenu() idx: "+index);
        if (idnpc == -53) {
            Menu_Mr_Ballard(conn, idnpc, idmenu, index);
            return;
        }
        if (idnpc == -56) {
            send_menu_select(conn, 119, new String[]{"Thông tin", "Bảo hộ", "Hồi máu", "Tăng tốc"});
            return;
        }
        if (idnpc >= 30000 && idmenu == Manager.gI().event) {
            Menu_MobEvent(conn, idnpc, idmenu, index);
            return;
        }
        switch (idnpc) {
            case 4: {
                Menu_DoiDongMeDaySTG(conn, index);
                break;
            }
            case 5: {
                Menu_DoiDongMeDaySTPT(conn, index);
                break;
            }
            case 114: {
                Menu_Wedding(conn, index);
                break;
            }
            case 115: {
                Menu_Thongtincanhan(conn, index);
                break;
            }

            case 117: {
                Menu_ThaoKhamNgoc(conn, index);
                break;
            }
            case -54: {
                Menu_Mr_Haku(conn, index);
                break;
            }
            case -81: {
                Menu_Mrs_Oda(conn, index);
                break;
            }
            case -82: {
                if (conn.p.map.ld2 != null) {
                    Menu_Mrs_Oda_trong_LoiDai(conn, index);
                } else {
                    Menu_MissAnwen(conn, index);
                }
                break;
            }
            case 118: {
                Menu_View_LoiDai(conn, index);
                break;
            }
           
            case 210: {
                Menu_ThayDongCanh_percent(conn, index);
            }
            case 119: {
                Menu_Pet_di_buon(conn, index);
                break;
            }
            case -57: {
                Menu_Mr_Dylan(conn, index);
                break;
            }
            case -58: {
                Menu_Graham(conn, index);
                break;
            }
            case -59: {
                Menu_Mr_Frank(conn, index);
                break;
            }
            case 121: {
                Menu_TachCanh(conn, index);
                break;
            }
            case -3: { // Lisa
                Menu_Lisa(conn, index);
                break;
            }
            case 888: { // 
                Menu_BXHCLAN(conn, index);
                break;
            }
            case -20: {
                Menu_Emma(conn, index);
            }
            case -90: { // keva
                Menu_keva(conn, index);
                break;
            }
            case 600: {
                Menu_Langphusuongup(conn, index);
                break;
            }
            case 601: {
                Menu_Langphusuongboss(conn, index);
                break;
            }
            case -4: {
                Menu_Doubar(conn, index, idmenu);
                break;
            }
            case 345: {
                Menu_Doiaochoang(conn, index);
                break;
            }
            case 346: {
                Menu_Doiaochoang1(conn, index);
                break;
            }
            case 347: {
                Menu_Doiaochoang2(conn, index);
                break;
            }
            case 348: {
                Menu_Doiaochoang3(conn, index);
                break;
            }
            case 349: {
                Menu_Doiaochoang4(conn, index);
                break;
            }
            case 350: {
                Menu_Doiaochoang5(conn, index);
                break;
            }
            case 351: {
                Menu_Doiaochoang6(conn, index);
                break;
            }
            case 777: {
                Menu_Doiaochoang7(conn, index);
                break;
            }
            case -5: {
                Menu_Hammer(conn, index, idmenu);
                break;
            }
            case -22: {
                Menu_Alisama(conn, index, idmenu);
                break;
            }
            case -33: {
                Menu_DaDichChuyen33(conn, index);
                break;
            }
            case -55: {
                Menu_DaDichChuyen55(conn, index);
                break;
            }
            case -10: {
                Menu_DaDichChuyen10(conn, index);
                break;
            }
            case 1000: {
                Menu_GiapSieuNhan(conn, index);
                break;
            }

            case 1001: {
                Menu_Quest_Daily(conn, index);
                break;
            }
            case -77: {
                Menu_Alisama(conn, index);
                break;
            }
            case -8: {
                Menu_Zulu(conn, index);
                break;
            }
            case 126: {
                Menu_Admin(conn, index);
                break;
            }
            case -36: {
                Menu_PhapSu(conn, index);
                break;
            }
            case -44: {
                Menu_Miss_Anna(conn, index);
                break;
            }
            case -32: {
                Menu_BXH(conn, index);
                break;
            }
            case -21: {
                Menu_Black_Eye(conn, index);
                break;
            }

            case -7: {
                Menu_Aman(conn, index);
                break;
            }
            case -34: {
                Menu_CuopBien(conn, index);
                break;
            }
            case 125: { // vxmm
                Menu_VXMM(conn, index);
                break;
            }
            case 132: { // vxmm
                Menu_VXKC(conn, index);
                break;
            }

            case -2: { // vxmm
                Menu_Zoro(conn, index);
                break;
            }
            case -19: { // vxmm
                Menu_Benjamin(conn, index);
                break;
            }
            case -85: { //
                Menu_Mr_Edgar(conn, index);
                break;
            }
            case 124: {
                Service.revenge(conn, index);
                break;
            }
            case 123: {
                Menu_Dungeon_Mode_Selection(conn, index);
                break;
            }
            case 122: {
                Menu_Clan_Manager(conn, index);
                break;
            }
            case -42: {
                Menu_Pet_Manager(conn, index);
                break;
            }
            case -37: {
                Menu_PhoChiHuy(conn, index);
                break;
            }
            case -38:
            case -40: {
                Menu_LinhCanh(conn, index);
                break;
            }
            case -41: {
                Menu_TienCanh(conn, index);
                break;
            }
            case -49: {
                Menu_top(conn, index);
                break;
            }
//            case -81: {
//                Menu_diempk(conn, index);
//                break;
//            }
            case -69: {
                if (Manager.gI().event == 1) {
                    Menu_Event(conn, index);
                }
                if (Manager.gI().event == 2) {
                    Menu_MissSophia(conn, idnpc, idmenu, index);
                }
                if (Manager.gI().event == 3) {
                    Menu_MissSophia(conn, idnpc, idmenu, index);
                }
                break;
            }
            case -62: {
                if (Manager.gI().event == 1) {
                    Menu_NauKeo(conn, index);
                }
                break;
            }
            case -66: {
                if (Manager.gI().event == 1) {
                    Menu_CayThong(conn, index);
                }

                break;
            }
            case 120: {
                break;
            }
            case -91: {
                Menu_Khac(conn, idmenu, index);
                break;
            }
            case -101: {
                Menu_Krypton(conn, idmenu, index);
                break;
            }
            case -103: {

                break;
            }

            default: {
                Service.send_notice_box(conn, "Đã xảy ra lỗi");
                break;
            }
        }
    }

    private static void Menu_Mr_Ballard(Session conn, int idNPC, byte idmenu, byte index) throws IOException {
        switch (idmenu) {
            case 0: {
                switch (index) {
                    case 0: { // dang ky
                        if (ChienTruong.gI().getStatus() == 1) {
                            ChienTruong.gI().register(conn.p);
                        } else {
                            Service.send_notice_box(conn, "Không trong thời gian diễn ra");
                        }
                        break;
                    }
                    case 1: {
                        String s = "Đăng ký tại NPC Mr. Ballard ở map Hang Lửa trước thời gian bắt đầu 45 phút";
                        s += "Phí tham gia 2 ngọc hoặc 500.000 vàng";
                        s += "Người chơi phải từ level 40 trở lên";
                        s += "Cách chơi :";
                        s += " Người chơi được ngẫu nhiên chia đều làm 4 phe, khi gần đến giờ người chơi sẽ được chuyển đến map tập trung .";
                        s += "Đến đúng giờ diễn ra sự kiện , map tập trung sẽ mở cửa để đi sang các map khác.";

                        s += "Mỗi phe sẽ có một map tập trung của phe mình và ít nhất 1 map nhà chính , điều này tùy thuộc vào số người đăng ký . Tối đa 25 map";

                        s += "Ví dụ : tổng số người đăng ký là 40 người - mỗi phe 10 người - thì mỗi phe sẽ sở hữu 1 nhà chính . Nếu tổng số người đăng ký là 80 người - mỗi phe 20 người - thì mỗi phe sẽ sở hữu 2 nhà chính.";

                        s += "\nTại mỗi map nhà chính chỉ có thể cho phép 40 người vào cùng 1 lượt và chia đều 10 người mỗi phe. Nghĩa là tại mỗi map nhà chính bất kỳ chỉ có thể vào được 10 người trong cùng 1 phe.";
                        s += "\nTại mỗi map nhà chính sẽ có 10 lính canh , 10 lính canh này sẽ đánh tất cả những người nào khác phe của nó";
                        s += "\nNhững map nhà chính sẽ được nối với 1 map chung , tại map chung phút thứ 5 và phút thứ 10 sẽ xuất hiện Boss Xà Nữ, nếu Boss thứ nhất ở phút thứ 5 đầu tiên đến phút thứ 10 vẫn chưa bị chết thì vẫn xuất hiện Boss thứ 2. Đánh chết boss Xà Nữ sẽ được điểm chiến trường";

                        s += "\n4. Điều kiện chiến thắng :";
                        s += "Nếu chưa hết 60 phút mà đã có 3 phe sập hết nhà chính, chỉ còn lại 1 phe còn nhà chính thì phe đó là phe thắng cuộc, sự kiện sẽ kết thúc. Phần thưởng sẽ là tổng số ngọc đăng ký của tất cả các phe chia đều cho những người còn sống trong phe thắng cuộc.";
                        s += " Khi hết 60 phút mà chưa có phe nào chiến thắng thì phần thưởng sẽ là ngọc đăng ký chia đều cho tất cả những người còn sống trong đấu trường.";
                        s += "Người chơi nhớ vào NPC Lisa hoặc Emma để nhận quà";

                        s += "Điểm chiến trường";
                        s += " Cách kiếm điểm chiến trường gồm";
                        s += "Tiêu diệt một người chơi khác phe : 1 điểm";
                        s += "Giết quái hoặc một lính canh khác phe : 2 điểm";
                        s += "Tiêu diệt một nhà khác phe bất kì : 20 điểm";
                        s += "Sử dụng item chiến trường : 10 điểm";
                        s += "Đánh chết Boss Xà Nữ : 30 điểm";
                        Service.send_notice_box(conn, s);
                        break;
                    }
                    case 3: {
                        if (ChienTruong.gI().getStatus() == 2) {
                            if (conn.p.time_use_item_arena > System.currentTimeMillis()) {
                                Service.send_notice_box(conn,
                                        "Chờ sau " + (conn.p.time_use_item_arena - System.currentTimeMillis()) / 1000 + " s");
                                return;
                            }
                            Member_ChienTruong info = ChienTruong.gI().get_infor_register(conn.p.name);
                            if (info != null) {
                                conn.p.time_use_item_arena = System.currentTimeMillis() + 250_000;
                                Vgo vgo = new Vgo();
                                switch (info.village) {
                                    case 2: { // lang gio
                                        vgo.id_map_go = 55;
                                        vgo.x_new = 224;
                                        vgo.y_new = 256;
                                        MapService.change_flag(conn.p.map, conn.p, 2);
                                        break;
                                    }
                                    case 3: { // lang lua
                                        vgo.id_map_go = 59;
                                        vgo.x_new = 240;
                                        vgo.y_new = 224;
                                        MapService.change_flag(conn.p.map, conn.p, 1);
                                        break;
                                    }
                                    case 4: { // lang set
                                        vgo.id_map_go = 57;
                                        vgo.x_new = 264;
                                        vgo.y_new = 272;
                                        MapService.change_flag(conn.p.map, conn.p, 4);
                                        break;
                                    }
                                    default: { // 5 lang anh sang
                                        vgo.id_map_go = 53;
                                        vgo.x_new = 276;
                                        vgo.y_new = 246;
                                        MapService.change_flag(conn.p.map, conn.p, 5);
                                        break;
                                    }
                                }
                                conn.p.change_map(conn.p, vgo);
                            } else {
                                Service.send_notice_box(conn, "Chưa đăng ký");
                            }
                            // Vgo vgo = new Vgo();
                            // vgo.id_map_go = 61;
                            // vgo.x_new = 432;
                            // vgo.y_new = 354;
                            // conn.p.change_map(conn.p, vgo);
                        } else {
                            Service.send_notice_box(conn, "Không trong thời gian diễn ra");
                        }
                        break;
                    }
                    case 2: {
                        if (conn.p.pointarena < 5000) {
                            Service.send_notice_box(conn, "Phải cần tối thiểu 5000 điểm tích lũy chiến trường để có thể đổi trứng đại bàng.");
                        } else if (conn.p.item.get_bag_able() < 1) {
                            Service.send_notice_box(conn, "Cần tối thiểu 1 ô trống để có thể đổi.");
                        } else {
                            try ( Connection connection = SQL.gI().getConnection();  Statement st = connection.createStatement();  ResultSet rs = st.executeQuery("SELECT * FROM `history_doi_dai_bang` WHERE `user` = '" + conn.user + "' AND `time` >= DATE_SUB(NOW(), INTERVAL 1 WEEK);")) {
                                if (rs.next()) {
                                    Service.send_notice_box(conn, "Trong vòng 1 tuần 1 tài khoản chỉ có thể đổi 1 lần.");
                                    return;
                                } else {
                                    int last_point = conn.p.pointarena;
                                    short iditem = 3269;
                                    Item3 itbag = new Item3();
                                    itbag.id = iditem;
                                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                                    itbag.op = new ArrayList<>();
                                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                                    itbag.tier = 0;
                                    itbag.islock = false;
                                    itbag.time_use = 0;
                                    conn.p.item.add_item_bag3(itbag);
                                    conn.p.pointarena -= 1200;
                                    conn.p.item.char_inventory(3);
                                    String query
                                            = "INSERT INTO `history_doi_dai_bang` (`user`, `name_player`, `last_point` , `point_arena`) VALUES ('"
                                            + conn.user + "', '" + conn.p.name + "', '" + last_point + "', '" + conn.p.pointarena + "')";
                                    if (st.executeUpdate(query) > 0) {
                                        connection.commit();
                                    }
                                    List<box_item_template> ids = new ArrayList<>();
                                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                        break;

                    }
                    default:
                        Service.send_notice_box(conn, "Chức năng đang được hoàn thiện.");
                        break;
                }
                break;
            }
            case 1: {
                Service.send_notice_box(conn, "Chức năng đang được hoàn thiện.");
                break;
            }
            //  }
        }

    }

    private static void Menu_MissSophia(Session conn, int idNPC, byte idmenu, byte index) throws IOException {
//        System.out.println("core.MenuController.Menu_MissSophia() id: "+idmenu);
//        System.out.println("core.MenuController.Menu_MissSophia() idx: "+index);
//        System.out.println("core.MenuController.Menu_MissSophia() ev: "+Manager.gI().event);
        if (idmenu == 2 && Manager.gI().event == 2) {
            switch (index) {
                case 0: {
                    if (conn.p.level < 40) {
                        Service.send_notice_box(conn, "Level quá thấp.");
                        return;
                    }
                    if (conn.p.item.get_bag_able() < 4) {
                        Service.send_notice_box(conn, "Hành trang đầy");
                        return;
                    }
                    if (conn.p.item.total_item_by_id(4, 141) < 1 && (!Manager.BuffAdminMaterial || conn.ac_admin < 4)) {
                        Service.send_notice_box(conn, "Thiếu " + template.ItemTemplate4.item.get(141).getName());
                        return;
                    }
                    for (int i = 254; i <= 258; i++) {
                        if (conn.p.item.total_item_by_id(4, i) < 1 && (!Manager.BuffAdminMaterial || conn.ac_admin < 4)) {
                            Service.send_notice_box(conn, "Thiếu " + template.ItemTemplate4.item.get(i).getName());
                            return;
                        }
                    }

                    conn.p.item.remove(4, 141, 1);
                    for (int i = 254; i <= 258; i++) {
                        conn.p.item.remove(4, i, 1);
                    }
                    List<box_item_template> ids = new ArrayList<>();

                    List<Integer> it7 = new ArrayList<>(java.util.Arrays.asList(0, 1, 4, 8, 9, 10, 11, 12, 13, 14));
                    List<Integer> it7_vip = new ArrayList<>(java.util.Arrays.asList(33, 346, 347, 349));
                    List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList(2, 5, 61, 67, 269));
                    List<Integer> it4_vip = new ArrayList<>(java.util.Arrays.asList(131, 123, 132, 133, 52, 235, 147));
                    for (int i = 0; i < Util.random(1, 5); i++) {
                        int ran = Util.random(100);
                        if (ran < 0) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 2) {
                            short idsach = (short) Util.random(4577, 4585);
                            ids.add(new box_item_template(idsach, (short) 1, (byte) 3));
                            conn.p.item.add_item_bag3_default(idsach, 0, false);
                        } else if (ran < 6) {
                            short idsach = (short) 4762;
                            ids.add(new box_item_template(idsach, (short) 1, (byte) 3));
                            conn.p.item.add_item_bag3_default(idsach, Util.random(10, 20), true);
                        } else if (ran < 14) {
                            short id = (short) Util.random(46, 246);
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 24) {
                            short id = (short) Util.random(417, 464);
                            short quant = (short) Util.random(3);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 41) {
                            short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 2);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 57) {
                            short id = Util.random(it4_vip, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 2);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 77) {
                            short id = Util.random(it4, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        }
                    }
                    ev_he.Event_2.add_caythong(conn.p.name, 1);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                }
                case 1: {
                    send_menu_select(conn, 120, ev_he.Event_2.get_top());
                    break;
                }
                case 2: {
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Hành trang đầy");
                        return;
                    }
                    if (conn.p.item.total_item_by_id(4, 123) < 5) {
                        Service.send_notice_box(conn, "Cần tối thiểu 5 chuông vàng");
                        return;
                    }
                    List<box_item_template> ids = new ArrayList<>();
                    conn.p.item.remove(4, 123, 5);
                    List<Integer> it = new ArrayList<>(java.util.Arrays.asList(4612, 4632, 4633, 4634, 4635));
                    List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList(299, 205, 207));
                    if (Util.random(100) < 60) {
                        short id = Util.random(it4, new ArrayList<>()).shortValue();
                        short quant = (short) Util.random(1, 3);
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_box47(id, quant, (byte) 4);
                    } else {
                        short id = Util.random(it, new ArrayList<>()).shortValue();
                        ids.add(new box_item_template(id, (short) 1, (byte) 3));
                        conn.p.item.add_item_bag3_default(id, Util.random(5, 7), true);
                    }

                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng ev2!");
                    break;
            }
        } else if (idmenu == 3 && Manager.gI().event == 3) {
            switch (index) {
                case 0: {
                    Service.send_box_input_text(conn, 25, "Đổi bó sen trắng", new String[]{"30 sen trắng + 100k vàng"});
                    break;
                }
                case 1: {
                    Service.send_box_input_text(conn, 26, "Đổi hoa sen hồng", new String[]{"10 sen trắng + 25k vàng"});
                    break;
                }
                case 2: {
                    Service.send_box_input_text(conn, 27, "Đổi bó sen hồng", new String[]{"5 sen hồng + 100 ngọc"});
                    break;
                }
                case 3: {
                    send_menu_select(conn, 120, ev_he.Event_3.get_top());
                    break;
                }
                case 4: {
                    if (conn.p.get_ngoc() < 100 || conn.p.item.total_item_by_id(4, 304) < 10) {
                        Service.send_notice_box(conn, "Cần tối thiểu 100 ngọc và 10 bông sen hồng để đổi!");
                        return;
                    }
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Không đủ ô trống!");
                        return;
                    }
                    conn.p.update_ngoc(-100);
                    conn.p.item.remove(4, 304, 10);
                    Item47 itbag = new Item47();
                    itbag.id = 246;
                    itbag.quantity = (short) 100;
                    itbag.category = 4;
                    conn.p.item.add_item_bag47(4, itbag);
                    conn.p.item.char_inventory(5);

                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{246}, new int[]{100}, new short[]{4});
                    break;
                }
                case 5: {
                    if (conn.p.get_ngoc() < 200 || conn.p.item.total_item_by_id(4, 304) < 50) {
                        Service.send_notice_box(conn, "Cần tối thiểu 200 ngọc và 50 bông sen hồng để đổi!");
                        return;
                    }
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Không đủ ô trống!");
                        return;
                    }
                    conn.p.update_ngoc(-200);
                    conn.p.item.remove(4, 304, 50);
                    short iditem = 3616;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.tier = 0;
                    itbag.islock = false;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 15;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.item.char_inventory(5);

                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                }
                case 6: {
                    if (conn.p.get_ngoc() < 200 || conn.p.item.total_item_by_id(4, 304) < 50) {
                        Service.send_notice_box(conn, "Cần tối thiểu 200 ngọc và 50 bông sen hồng để đổi!");
                        return;
                    }
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Không đủ ô trống!");
                        return;
                    }
                    conn.p.update_ngoc(-200);
                    conn.p.item.remove(4, 304, 50);
                    short iditem = 4761;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.tier = 0;
                    itbag.islock = false;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 15;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.item.char_inventory(5);

                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                }
                case 7: {
                    if (conn.p.get_ngoc() < 500 || conn.p.item.total_item_by_id(4, 304) < 50) {
                        Service.send_notice_box(conn, "Cần tối thiểu 500 ngọc và 50 bông sen hồng để đổi!");
                        return;
                    }
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Không đủ ô trống!");
                        return;
                    }
                    conn.p.update_ngoc(-500);
                    conn.p.item.remove(4, 304, 50);
                    short iditem = 4642;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.tier = 0;
                    itbag.islock = false;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.item.char_inventory(5);

                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng ev3!");
                    break;
            }
        } else {
            Service.send_notice_box(conn, "menu: " + idmenu + "  ev: " + Manager.gI().event);
        }

    }

    private static void Menu_MobEvent(Session conn, int idmob, byte idmenu, byte index) throws IOException {
        if (idmenu == 2) {
            if (index != 0) {
                return;
            }
            if (conn.p.level < 40) {
                Service.send_notice_box(conn, "Cần lên level 40 để có thể chơi sự kiện.");
                return;
            }
            ev_he.MobCay mob = ev_he.Event_2.getMob(idmob);
            if (mob == null || !mob.map.equals(conn.p.map)) {
                Message m2 = new Message(17);
                m2.writer().writeShort(-1);
                m2.writer().writeShort(idmob);
                conn.addmsg(m2);
                m2.cleanup();
                Service.send_notice_box(conn, "Không tìm thấy");
                return;
            }
            if (!(mob.map.equals(conn.p.map) && Math.abs(mob.x - conn.p.x) < 150 && Math.abs(mob.y - conn.p.y) < 150)) {
                Service.send_notice_box(conn, "Khoảng cách quá xa.\nNếu thực sự ở gần hãy thử load lại map.");
                return;
            }
            if (mob.Owner != null) {
                Service.send_notice_box(conn, "Đã có người khác hái quả.");
                return;
            }
            if (conn.p.item.get_bag_able() < 1) {
                Service.send_notice_nobox_white(conn, "Hành trang đầy.");
                return;
            }
            if (conn.p.item.total_item_by_id(4, 252) < 1) {
                Service.send_notice_nobox_white(conn, "Hãy mua giỏ hái quả để chứa.");
                return;
            }
            conn.p.item.remove(4, 252, 1);
            mob.setOwner(conn.p);
            short id = (short) Util.random(254, 259);
            conn.p.item.add_item_bag47(id, (short) 1, (byte) 4);
            conn.p.item.char_inventory(4);
            Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{1}, new short[]{4});
            //Service.send_notice_box(conn, "Nhận quả: "+mob.nameOwner);
        }
    }

    private static void Menu_Krypton(Session conn, byte idmenu, byte index) throws IOException {
        if (idmenu == 0)//nâng mề dùng đá Krypton
        {
            GameSrc.UpgradeMedal(conn, index);
        } else if (idmenu == 1) {
            GameSrc.UpgradeItemStar(conn, index);
        }
        conn.p.id_Upgrade_Medal_Star = -1;
    }

    private static void Menu_Khac(Session conn, byte idmenu, byte index) throws IOException {
        if (idmenu == 0) {
            switch (index) {
                case 0: {
                    if (conn.p.item.total_item_by_id(4, 52) > 0) {
                        MoLy.show_table_to_choose_item(conn.p);
                    } else {
                        Service.send_notice_box(conn, "Không đủ vé mở trong hành trang");
                    }
                    break;
                }
                case 1: {
                    String[] menu = new String[]{"Vòng xoay Vàng", "Vòng xoay ngọc", "Lịch sử","Tài Xĩu"};
                    MenuController.send_menu_select(conn, -34, menu);
                    break;
                }
                case 2: {
                    Service.send_notice_box(conn, "Bạn đang có " + conn.p.hieuchien + " Điểm Pk.");
                    break;
                }
                case 3: {
                    if (conn.p.map.ld2 != null) {
                        Menu_Mrs_Oda_trong_LoiDai(conn, (byte) 0);
                    } else {
                        if (conn.p.item.wear[11] != null && (conn.p.item.wear[11].id == 3599 || conn.p.item.wear[11].id == 3593 || conn.p.item.wear[11].id == 3596)) {
                            Service.send_notice_box(conn, "Bạn không thể về làng nhanh khi đang mặc loại giáp liên quan đến chức năng buôn");
                            return;
                        }
                        Vgo vgo = new Vgo();
                        vgo.id_map_go = 1;
                        vgo.x_new = 432;
                        vgo.y_new = 354;
                        conn.p.change_map(conn.p, vgo);
                    }
                    break;
                }
                case 4: {
                    conn.p.isDropMaterialMedal = !conn.p.isDropMaterialMedal;
                    Service.send_notice_box(conn, "Rơi nguyên liệu mề đay đã " + (conn.p.isDropMaterialMedal ? "bật" : "tắt"));
                    break;
                }
                case 5: {
                    if (conn.p.map.ld2 != null) {
                        LoiDaiManager.gI().GetMenuViews(conn, conn.p.map.ld2.idxGroup);
                    } else {
                        Service.send_notice_box(conn, "Bạn chỉ có thể thao tác khi đang ở trong map lôi đài");
                    }
                    break;
                }
                case 6: {
                    conn.p.isShowMobEvents = !conn.p.isShowMobEvents;
                    Service.send_notice_box(conn, "Đã " + (conn.p.isShowMobEvents ? "bật" : "tắt") + " hiển thị cây sự kiện");
                    break;

                }
            }
        }

    }

    private static void Menu_View_LoiDai(Session conn, byte index) throws IOException {
        Service.send_notice_box(conn, "Chức năng chưa hoàn thiện");
        //LoiDaiManager.gI().JoinMap(conn.p, index);
    }

    private static void Menu_Mrs_Oda_trong_LoiDai(Session conn, byte index) throws IOException {
        if (conn.p.map.ld2 == null) {
            return;
        }
        switch (index) {
            case 0: {
                if (conn.p.map.ld2.pl_1 != null && conn.p.map.ld2.pl_2 != null && (conn.p.map.ld2.pl_1.index == conn.p.index || conn.p.map.ld2.pl_2.index == conn.p.index)) {
                    Service.send_notice_box(conn, "Bạn Không thể Thực hiện hành động này");
                    return;
                }
                Vgo vgo = new Vgo();
                vgo.id_map_go = 1;
                vgo.x_new = 432;
                vgo.y_new = 354;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 1: {
                LoiDaiManager.gI().Info(conn);
                break;
            }
            case 2: {
                LoiDaiManager.gI().GetGroup(conn.p.map.ld2.idxGroup).InfoGroup(conn);
                break;
            }
            case 3: {
                LoiDaiManager.gI().GetGroup(conn.p.map.ld2.idxGroup).GetRewarded(conn);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chức năng chưa hoàn thiện");
//                if(conn.p.map.ld2.pl_1!=null && conn.p.map.ld2.pl_2!=null && (conn.p.map.ld2.pl_1.id == conn.p.id || conn.p.map.ld2.pl_2.id == conn.p.id)){
//                    Service.send_notice_box(conn, "Bạn Không thể Thực hiện hành động này");
//                    return;
//                }
//                LoiDaiManager.gI().JoinMapPVP(conn.p,conn.p.map.ld2.idxGroup, index-1);
                break;
            }
        }
    }

    private static void Menu_Mrs_Oda(Session conn, byte index) throws IOException {
        if (conn.p.point_active.length < 3) {
            int a0 = conn.p.point_active[0];
            int a1 = conn.p.point_active[1];
            conn.p.point_active = new int[]{a0, a1, 0};
        }
//        if (index >= 0 && index <= 5) {
//            Service.send_notice_box(conn, "Chưa có chức năng :(");
//            return;
//        }
        switch (index) {
            case 0: {
                LoiDaiManager.gI().JoinGroup(conn.p);
                break;
            }
            case 1: {
                LoiDaiManager.gI().JoinMap(conn.p);
                break;
            }
            case 2: {
                Service.send_notice_box(conn, "Chưa có chức năng.");
//                String[] smenu = new String[LoiDaiManager.Group_entrys.length];
//                int lv = 65;
//                for(int i=0;i<LoiDaiManager.Group_entrys.length;i++){
//                    smenu[i] = "Nhóm "+lv+" - "+(lv+9);
//                    lv+=10;
//                }
//                send_menu_select(conn, 118, smenu);
                break;
            }
            case 3: {
//                String notice = "Hiện tại : \n";
//                switch (LoiDai.state) {
//                    case 0: {
//                        notice += "thời gian đăng ký còn " + (LoiDai.time_state) + "s nữa\n";
//                        notice += "Danh sách đăng ký : \n" + LoiDai.get_list_register();
//                        break;
//                    }
//                    case 1: {
//                        notice += "thời gian diễn ra lôi đài được " + (0 - LoiDai.time_state) + "s";
//                        break;
//                    }
//                    case 2: {
//                        notice += LoiDai.get_notice_break_time();
//                        break;
//                    }
//                }
//                Service.send_notice_box(conn, notice);
                LoiDaiManager.gI().Info(conn);
                break;
            }
            case 4: {
                Service.send_notice_box(conn, "Điểm lôi đài hiện tại: " + LoiDaiManager.gI().GetDiemLD(conn.p.index));
                break;
            }
            case 5: {
                String s = "Lôi đài bắt đầu mở đăng kí vào lúc 18h00, và đóng đăng kí vào lúc 18h28.";
                s += "\n18h30 bắt đầu giao chiến.";
                s += "\nCó 10 vòng đấu, mỗi vòng 3p.";
                s += "\nNghỉ 1p giữa các vòng.";
                s += "\nKết thúc lôi đài người giữ top1 hãy vào lôi đài để nhận quà.";
                s += "\nThời gian nhận quà là từ 20h đến 23h59.";
                s += "\nLưu ý: khi đã có tên trong lôi đài, mà lever nhân vật thay đổi dẫn đến việc không còn nằm trong nhóm đã đăng kí ban đầu thì có thể không nhận được quà và ghép trận";
                Service.send_notice_box(conn, s);
                break;
            }
            case 6: {
                conn.p.list_thao_kham_ngoc.clear();
                for (int i = 0; i < conn.p.item.wear.length; i++) {
                    Item3 it = conn.p.item.wear[i];
                    if (it != null) {
                        short[] b = conn.p.item.check_kham_ngoc(it);
                        boolean check = false;
                        if ((b[0] != -2 && b[0] != -1) || (b[1] != -2 && b[1] != -1) || (b[2] != -2 && b[2] != -1)) {
                            check = true;
                        }
                        if (check) {
                            conn.p.list_thao_kham_ngoc.add(it);
                        }
                    }
                }
                String[] list_show = new String[]{"Trống"};
                if (conn.p.list_thao_kham_ngoc.size() > 0) {
                    list_show = new String[conn.p.list_thao_kham_ngoc.size()];
                    for (int i = 0; i < list_show.length; i++) {
                        list_show[i] = conn.p.list_thao_kham_ngoc.get(i).name;
                    }
                }
                MenuController.send_menu_select(conn, 117, list_show);
                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Pet_di_buon(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                String notice = null;
                if (conn.p.pet_di_buon != null && conn.p.pet_di_buon.item.size() > 0) {
                    notice = "%s " + ItemTemplate3.item.get(3590).getName() + "\n";
                    notice += "%s " + ItemTemplate3.item.get(3591).getName() + "\n";
                    notice += "%s " + ItemTemplate3.item.get(3592).getName() + "\n";
                    int n1 = 0, n2 = 0, n3 = 0;
                    for (int i = 0; i < conn.p.pet_di_buon.item.size(); i++) {
                        if (conn.p.pet_di_buon.item.get(i) == 3590) {
                            n1++;
                        } else if (conn.p.pet_di_buon.item.get(i) == 3591) {
                            n2++;
                        } else {
                            n3++;
                        }
                    }
                    notice = String.format(notice, n1, n2, n3);
                } else {
                    notice = "Trống";
                }
                Service.send_notice_box(conn, notice);
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                if (conn.p.get_ngoc() > 5) {
                    conn.p.pet_di_buon.update_hp(conn.p, 100);
                } else {
                    Service.send_notice_box(conn, "Không đủ 5 ngọc");
                }
                break;
            }
            case 3: {
                if (conn.p.get_ngoc() > 5) {
                    conn.p.pet_di_buon.update_speed(conn.p);
                } else {
                    Service.send_notice_box(conn, "Không đủ 5 ngọc");
                }
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Mr_Frank(Session conn, byte index) throws IOException {
        if (conn.p.map.map_id != 17) {
            Service.send_notice_box(conn, "Nhầm rồi e ơi!");
            return;
        }
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 39);
                break;
            }
            case 1: {
                if (conn.p.pet_di_buon != null && Math.abs(conn.p.pet_di_buon.x - conn.p.x) < 75
                        && Math.abs(conn.p.pet_di_buon.y - conn.p.y) < 75 && conn.p.item.wear[11] != null
                        && conn.p.item.wear[11].id == 3593) {
                    //
                    int vang_recei = 0;
                    for (int i = 0; i < conn.p.pet_di_buon.item.size(); i++) {
                        if (conn.p.pet_di_buon.item.get(i) == 3590) {
                            vang_recei += 50_000;
                        } else if (conn.p.pet_di_buon.item.get(i) == 3591) {
                            vang_recei += 75_000;
                        } else if (conn.p.pet_di_buon.item.get(i) == 3592) {
                            vang_recei += 100_000;
                        }
                    }
                    if (vang_recei > 0) {
                        conn.p.update_vang(vang_recei);
                        conn.p.item.char_inventory(5);
                        //
                        Message mout = new Message(8);
                        mout.writer().writeShort(conn.p.pet_di_buon.index);
                        for (int i = 0; i < conn.p.map.players.size(); i++) {
                            Player p0 = conn.p.map.players.get(i);
                            if (p0 != null) {
                                p0.conn.addmsg(mout);
                            }
                        }
                        mout.cleanup();
                        //
                        Pet_di_buon_manager.remove(conn.p.pet_di_buon.name);
                        conn.p.pet_di_buon = null;
                        Service.send_notice_box(conn, "Nhận được " + vang_recei + " vàng!");
                    } else {
                        Service.send_notice_box(conn, "Chưa cướp được gì cả, thật kém cỏi!");
                    }
                } else {
                    Service.send_notice_box(conn, "Ta không thấy con vật đi buôn của ngươi");
                }
                break;
            }
            case 2: {
                Item3 itbag = new Item3();
                itbag.id = 3593;
                itbag.clazz = ItemTemplate3.item.get(3593).getClazz();
                itbag.type = ItemTemplate3.item.get(3593).getType();
                itbag.level = ItemTemplate3.item.get(3593).getLevel();
                itbag.icon = ItemTemplate3.item.get(3593).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(3593).getOp());
                itbag.color = 5;
                itbag.part = ItemTemplate3.item.get(3593).getPart();
                itbag.tier = 0;
                itbag.islock = true;
                itbag.time_use = 0;
                // thao do
                if (conn.p.item.wear[11] != null && conn.p.item.wear[11].id != 3593 && conn.p.item.wear[11].id != 3599
                        && conn.p.item.wear[11].id != 3596) {
                    Item3 buffer = conn.p.item.wear[11];
                    conn.p.item.wear[11] = null;
                    conn.p.item.add_item_bag3(buffer);
                }
                itbag.name = ItemTemplate3.item.get(3593).getName() + " [Khóa]";
                itbag.UpdateName();
                conn.p.item.wear[11] = itbag;
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(3);
                conn.p.fashion = Part_fashion.get_part(conn.p);
                conn.p.change_map_di_buon(conn.p);
                Service.send_notice_box(conn, "Nhận thành công");
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Graham(Session conn, byte index) throws IOException {
        if (conn.p.map.map_id != 8) {
            return;
        }
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 32);
                break;
            }
            case 1: {
                if (conn.p.pet_di_buon != null && Math.abs(conn.p.pet_di_buon.x - conn.p.x) < 75
                        && Math.abs(conn.p.pet_di_buon.y - conn.p.y) < 75 && conn.p.item.wear[11] != null
                        && conn.p.item.wear[11].id == 3599) {
                    //
                    int vang_recei = 0;
                    for (int i = 0; i < conn.p.pet_di_buon.item.size(); i++) {
                        if (conn.p.pet_di_buon.item.get(i) == 3590) {
                            vang_recei += 250_000;
                        } else if (conn.p.pet_di_buon.item.get(i) == 3591) {
                            vang_recei += 350_000;
                        } else if (conn.p.pet_di_buon.item.get(i) == 3592) {
                            vang_recei += 450_000;
                        }
                    }
                    if (vang_recei > 0) {
                        conn.p.update_vang(vang_recei);
                        conn.p.item.char_inventory(5);
                        //
                        Message mout = new Message(8);
                        mout.writer().writeShort(conn.p.pet_di_buon.index);
                        for (int i = 0; i < conn.p.map.players.size(); i++) {
                            Player p0 = conn.p.map.players.get(i);
                            if (p0 != null) {
                                p0.conn.addmsg(mout);
                            }
                        }
                        mout.cleanup();
                        //
                        Pet_di_buon_manager.remove(conn.p.pet_di_buon.name);
                        conn.p.pet_di_buon = null;
                        Service.send_notice_box(conn, "Nhận được " + vang_recei + " vàng!");
                    } else {
                        Service.send_notice_box(conn, "Ngươi chưa có gì mà hay bị cướp mất hết hàng rồi!");
                    }
                } else {
                    Service.send_notice_box(conn, "Ta không thấy con vật đi buôn của ngươi");
                }
                break;
            }
            case 2: {
                Item3 itbag = new Item3();
                itbag.id = 3599;
                itbag.clazz = ItemTemplate3.item.get(3599).getClazz();
                itbag.type = ItemTemplate3.item.get(3599).getType();
                itbag.level = ItemTemplate3.item.get(3599).getLevel();
                itbag.icon = ItemTemplate3.item.get(3599).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(3599).getOp());
                itbag.color = 5;
                itbag.part = ItemTemplate3.item.get(3599).getPart();
                itbag.tier = 0;
                itbag.islock = true;
                itbag.time_use = 0;
                // thao do
                if (conn.p.item.wear[11] != null && conn.p.item.wear[11].id != 3593 && conn.p.item.wear[11].id != 3599
                        && conn.p.item.wear[11].id != 3596) {
                    Item3 buffer = conn.p.item.wear[11];
                    conn.p.item.wear[11] = null;
                    conn.p.item.add_item_bag3(buffer);
                }
                itbag.name = ItemTemplate3.item.get(3599).getName() + " [Khóa]";
                itbag.UpdateName();
                conn.p.item.wear[11] = itbag;
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(3);
                conn.p.fashion = Part_fashion.get_part(conn.p);
                conn.p.change_map_di_buon(conn.p);
                Service.send_notice_box(conn, "Nhận thành công");
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Mr_Dylan(Session conn, byte index) throws IOException {
        if (conn.p.map.map_id != 52) {
            return;
        }

        if (conn.p.item.wear[11] == null || (conn.p.item.wear[11] != null && conn.p.item.wear[11].id != 3599)) {
            Service.send_notice_box(conn, "Con không phải là thương nhân");
            return;
        }
        if (conn.p.pet_di_buon != null && Math.abs(conn.p.pet_di_buon.x - conn.p.x) < 75
                && Math.abs(conn.p.pet_di_buon.y - conn.p.y) < 75) {
            switch (index) {
                case 0: {
                    Service.send_box_UI(conn, 31);
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        } else {
            Service.send_notice_box(conn, "Ta không thấy con vật đi buôn của ngươi");
        }
    }

    private static void Menu_NauKeo(Session conn, byte index) throws IOException {
        if (Manager.gI().event == 1) {
            switch (index) {
                case 0: {
                    // Service.send_box_input_text(conn, 11, "Nhập số lượng", new String[] {"Số lượng :"});
                    if (conn.p.get_ngoc() < 10) {
                        Service.send_notice_box(conn, "Không đủ 10 ngọc");
                        return;
                    }
                    if (Event_1.naukeo.time <= 30) {
                        Service.send_notice_box(conn, "Không thể tăng tốc");
                        return;
                    }
                    conn.p.update_ngoc(-10);
                    conn.p.item.char_inventory(5);
                    Event_1.naukeo.update(1);
                    Service.send_notice_box(conn, "Tăng tốc thành công");
                    break;
                }
                case 1: {
                    Service.send_notice_box(conn, "Nguyên liệu cần để nấu kẹo như sau: Đường, Sữa, Bơ, Vani\r\n"
                            + "- Mỗi ngày server cho nấu kẹo 1 lần vào lúc 17h , thời gian nấu là 2 tiếng.\r\n"
                            + "- Thời gian đăng ký là từ 19h ngày hôm trước đến 16h30 ngày hôm sau. Phí đăng ký là 5 ngọc\r\n"
                            + "- Một lần tăng tốc mất 10 ngọc và sẽ giảm được 2 phút nấu\r\n"
                            + "- Số kẹo tối đa nhận được là 20 kẹo.Tuy nhiên nếu các hiệp sĩ góp càng nhiều thì càng có lợi vì 10 người chơi góp nhiều nguyên liệu nhất sẽ được cộng thêm 20 cái\r\n"
                            + "+ Số kẹo nhận được sẽ tính theo công thức 1 Kẹo = 1 Đường + 1 Sữa + 1 Bơ+ 1 Vani");
                    break;
                }
                case 2: {
                    Service.send_notice_box(conn,
                            "Thông tin:\nĐã góp : " + Event_1.get_keo_now(conn.p.name) + "\nThời gian nấu còn lại : "
                            + ((Event_1.naukeo.time == 0) ? "Không trong thời gian nấu"
                                    : ("Còn lại " + Event_1.naukeo.time + "p")));
                    break;
                }
                case 3: {
                    send_menu_select(conn, 120, Event_1.get_top_naukeo());
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        }
    }

    private static void Menu_Event(Session conn, byte index) throws IOException {
        if (Manager.gI().event == 1) {
            switch (index) {
                case 0: {
                    Service.send_box_input_text(conn, 10, "Nhập số lượng", new String[]{"Số lượng :"});
                    break;
                }
                case 1: {
                    Service.send_notice_box(conn,
                            "Để đổi thành Hộp đồ chơi hoàn chỉnh theo công thức: 20.000 vàng + 50 Bức tượng rồng + 50 Kiếm đồ chơi + 50 Đôi giày nhỏ xíu + 50 Trang phục tí hon + 50 Mũ lính chì."
                            + "\nĐể đổi thành Túi kẹo hoàn chỉnh theo công thức: 50.000 vàng + 5 Kẹo.");
                    break;
                }
                case 2: {
                    if (!Event_1.check_time_can_register()) {
                        Service.send_notice_box(conn, "Không trong thời gian đăng ký!");
                        return;
                    }
                    if (conn.p.get_ngoc() < 5) {
                        Service.send_notice_box(conn, "Không đủ 5 ngọc");
                        return;
                    }
                    if (Event_1.check(conn.p.name)) {
                        Service.send_notice_box(conn, "Đã đăng ký rồi, quên à!");
                        return;
                    }
                    conn.p.update_ngoc(-5);
                    conn.p.item.char_inventory(5);
                    Event_1.add_material(conn.p.name, 0);
                    Service.send_notice_box(conn, "Đăng ký thành công, có thể góp nguyên liệu rồi");
                    break;
                }
                case 3: {
                    if (!Event_1.check_time_can_register()) {
                        Service.send_notice_box(conn, "Không trong thời gian đăng ký!");
                        return;
                    }
                    if (Event_1.check(conn.p.name)) {
                        Service.send_box_input_text(conn, 11, "Nhập số lượng", new String[]{"Số lượng :"});
                    } else {
                        Service.send_notice_box(conn, "Chưa đăng ký nấu kẹo, hãy đăng ký!");
                    }
                    break;
                }
                case 4: {
                    int quant = Event_1.get_keo(conn.p.name);
                    if (quant > 0) {
                        quant = (quant > 20) ? 20 : quant;
                        if (Event_1.list_bxh_naukeo_name.contains(conn.p.name)) {
                            quant += 20;
                        }
                        quant *= 3;
                        Item47 it = new Item47();
                        it.category = 4;
                        it.id = 162;
                        it.quantity = (short) quant;
                        conn.p.item.add_item_bag47(4, it);
                        conn.p.item.char_inventory(4);
                        Service.send_notice_box(conn, "Nhận được " + quant + " kẹo");
                    } else {
                        Service.send_notice_box(conn, "Đã nhận rồi hoặc chưa tham gia!");
                    }
                    break;
                }
                case 5: {
                    Service.send_box_input_text(conn, 12, "Nhập số lượng", new String[]{"Số lượng :"});
                    break;
                }
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13: {
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Hành trang không đủ chỗ trống!");
                        return;
                    }
                    short[] id_receiv = new short[]{4626, 4761, 3610, 4636, 4709, 4710, 281, 3616};
                    short[] tuikeo_required = new short[]{120, 120, 60, 60, 30, 30, 15, 60};
                    short[] hopdochoi_required = new short[]{120, 120, 60, 60, 30, 30, 15, 60};
                    int[] ngoc_required = new int[]{360, 330, 60, 60, 60, 60, 15, 300};
                    if (tuikeo_required[index - 6] > conn.p.item.total_item_by_id(4, 157)) {
                        Service.send_notice_box(conn, "Không đủ " + tuikeo_required[index - 6] + " túi kẹo!");
                        return;
                    }
                    if (hopdochoi_required[index - 6] > conn.p.item.total_item_by_id(4, 158)) {
                        Service.send_notice_box(conn, "Không đủ " + hopdochoi_required[index - 6] + " hộp đồ chơi!");
                        return;
                    }
                    if (ngoc_required[index - 6] > conn.p.get_ngoc()) {
                        Service.send_notice_box(conn, "Không đủ " + ngoc_required[index - 6] + " ngọc!");
                        return;
                    }
                    if (index != 12) {
                        Item3 itbag = new Item3();
                        ItemTemplate3 it_temp = ItemTemplate3.item.get(id_receiv[index - 6]);
                        itbag.id = it_temp.getId();
                        itbag.name = it_temp.getName();
                        itbag.clazz = it_temp.getClazz();
                        itbag.type = it_temp.getType();
                        itbag.level = 10;
                        itbag.icon = it_temp.getIcon();
                        itbag.op = new ArrayList<>();
                        itbag.op.addAll(it_temp.getOp());
                        itbag.color = it_temp.getColor();
                        itbag.part = it_temp.getPart();
                        itbag.tier = 0;
                        itbag.islock = false;
                        itbag.time_use = 0;
                        conn.p.item.add_item_bag3(itbag);
                        Service.send_notice_box(conn, "Nhận được " + itbag.name + ".");
                    } else {
                        Item47 itbag = new Item47();
                        itbag.id = id_receiv[index - 6];
                        itbag.quantity = (short) 20;
                        itbag.category = 4;
                        conn.p.item.add_item_bag47(4, itbag);
                        Service.send_notice_box(conn, "Nhận được 20 xe trượt tuyết.");
                    }
                    conn.p.item.remove(4, 157, tuikeo_required[index - 6]);
                    conn.p.item.remove(4, 158, hopdochoi_required[index - 6]);
                    conn.p.update_ngoc(-ngoc_required[index - 6]);
                    conn.p.item.char_inventory(4);
                    conn.p.item.char_inventory(3);
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Đang được chuẩn bị");
                    break;
                }
            }
        }
    }

//    private static void Menu_diempk(Session conn, byte index) throws IOException {
//        switch (index) {
//            case 0: {
//                Service.send_notice_box(conn, "Bạn đang có " + conn.p.hieuchien + " Điểm Pk.");
//                break;
//            }
//            default: {
//                Service.send_notice_box(conn, "Chưa có chức năng");
//                break;
//            }
//        }
//    }
    private static void Menu_MissAnwen(Session conn, byte index) throws IOException {
        Vgo vgo = null;
        switch (index) {
            case 0: {
                vgo = new Vgo();
                vgo.id_map_go = 1;
                vgo.x_new = 432;
                vgo.y_new = 354;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Thongtincanhan(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Service.send_notice_box(conn, "Thông tin:\n Hiệp Sĩ Shine \n Địa Chỉ Ip:" + conn.ip + "\nTài Khoản:" + conn.user + "\nMật Khẩu Là:" + conn.pass + "\n Số Coin Còn Lại \n:" + conn.coin + "\n(Ghi Chú 0 = Đã Kích hoạt, 1 = Chưa Kích hoạt) \n :" + conn.status);
                break;

            }
 case 1: {
                Service.send_notice_box(conn, "Thông Tin: \n"+ "\nDame : " + conn.p.body.get_DameBase()+ "\nHp :" + conn.p.body.get_HpMax()+ "\nMp :" + conn.p.body.get_MpMax()+ "\nChí Mạng :" + conn.p.body.get_Crit()+ "\nXuyên Giáp :" + conn.p.body.get_Pierce()+"\nPhòng Thủ :" + conn.p.body.get_DefBase()+ "\nNé Đòn:" + conn.p.body.get_Miss()+ "\nPhản Sát Thương :" + conn.p.body.get_PhanDame());
                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_top(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.chucphuc == 1) {
                    conn.p.chucphuc = 0;
                    int ngoc_ = Util.random(1000, 2000);
                    int vang_ = Util.random(10000, 100000);
                    conn.p.update_ngoc(ngoc_);
                    conn.p.update_vang(vang_);
                    conn.p.item.char_inventory(5);
                    Service.send_notice_box(conn,
                            "Cảm ơn bạn đã like cho tôi, để tỏ lòng biết ơn tôi tặng bạn: " + ngoc_ + " ngọc," + vang_ + "Vàng.");
                } else {
                    Service.send_notice_box(conn, "Hôm nay bạn đã like rồi, tôi không có nhiều tiền để phát quà vậy đâu!");
                }
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 37);
                break;
            }

            case 2: {
                if (conn.p.level < 60) {
                    Service.send_notice_box(conn, "Yêu cầu level trên 60");
                    return;
                }
                send_menu_select(conn, 114, new String[]{"Cầu hôn", "Ly hôn", "Nâng cấp nhẫn", "Hướng dẫn"});
                break;
            }

            case 3: {
                send_menu_select(conn, 115, new String[]{"Chest Thông Tin Tài Khoản", "Thông Tin Bản Thân"});
                break;
            }
            //case 4: {
            //     send_menu_select(conn, 1001,
            //                new String[]{"Hướng dẫn", "Nhận nhiệm vụ", "Hủy nhiệm vụ", "Trả nhiệm vụ", "Kiểm tra"});
            //        break;
            //      }
            case 4: {
                if (conn.p.hieuchien < 1000) {
                    Service.send_notice_box(conn, "Chưa đủ 1000 điểm pk");
                    return;
                }
                conn.p.hieuchien -= 1000;
                int random = Util.random(100);
                if (random < 10) {
                    short id_ = 4718;
                    Item3 itbag = new Item3();
                    itbag.id = id_;
                    itbag.name = ItemTemplate3.item.get(id_).getName();
                    itbag.clazz = ItemTemplate3.item.get(id_).getClazz();
                    itbag.type = ItemTemplate3.item.get(id_).getType();
                    itbag.level = ItemTemplate3.item.get(id_).getLevel();
                    itbag.icon = ItemTemplate3.item.get(id_).getIcon();
                    itbag.op = ItemTemplate3.item.get(id_).getOp();
                    itbag.color = ItemTemplate3.item.get(id_).getColor();
                    itbag.part = ItemTemplate3.item.get(id_).getPart();
                    itbag.tier = 0;
                    itbag.islock = false;
                    itbag.time_use = 0;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.item.char_inventory(3);
                    Service.send_notice_box(conn, "Nhận được " + itbag.name);
                    return;
                } else if (random > 10 && random < 20) {
                    short id_ = 4719;
                    Item3 itbag = new Item3();
                    itbag.id = id_;
                    itbag.name = ItemTemplate3.item.get(id_).getName();
                    itbag.clazz = ItemTemplate3.item.get(id_).getClazz();
                    itbag.type = ItemTemplate3.item.get(id_).getType();
                    itbag.level = ItemTemplate3.item.get(id_).getLevel();
                    itbag.icon = ItemTemplate3.item.get(id_).getIcon();
                    itbag.op = ItemTemplate3.item.get(id_).getOp();
                    itbag.color = ItemTemplate3.item.get(id_).getColor();
                    itbag.part = ItemTemplate3.item.get(id_).getPart();
                    itbag.tier = 0;
                    itbag.islock = false;
                    itbag.time_use = 0;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.item.char_inventory(3);
                    Service.send_notice_box(conn, "Nhận được " + itbag.name);
                    return;
                } else if (random > 20 && random < 30) {
                    short id_ = 4709;
                    Item3 itbag = new Item3();
                    itbag.id = id_;
                    itbag.name = ItemTemplate3.item.get(id_).getName();
                    itbag.clazz = ItemTemplate3.item.get(id_).getClazz();
                    itbag.type = ItemTemplate3.item.get(id_).getType();
                    itbag.level = ItemTemplate3.item.get(id_).getLevel();
                    itbag.icon = ItemTemplate3.item.get(id_).getIcon();
                    itbag.op = ItemTemplate3.item.get(id_).getOp();
                    itbag.color = ItemTemplate3.item.get(id_).getColor();
                    itbag.part = ItemTemplate3.item.get(id_).getPart();
                    itbag.tier = 0;
                    itbag.islock = false;
                    itbag.time_use = 0;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.item.char_inventory(3);
                    Service.send_notice_box(conn, "Nhận được " + itbag.name);
                    return;
                } else if (random > 30) {
                    int vang = Util.random(5000, 100000);
                    int ngoc = Util.random(200, 400);
                    conn.p.update_vang(vang);
                    conn.p.update_ngoc(ngoc);
                    conn.p.item.char_inventory(5);
                    Service.send_notice_box(conn, "Nhận được " + vang + "Vàng. " + ngoc + "Ngọc");
                }
                break;
            }
           // case 5: {
            //    if (conn.status != 0) {
              //      Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
             //       return;
             //   }
             //   if (conn.p.get_ngoc() < 1000) {
              //      Service.send_notice_box(conn, "Không đủ 1000 ngọc");
            //        return;
            //    }
            //    conn.p.update_ngoc(-100);
//
            //    Vgo vgo = null;
           //     vgo = new Vgo();
            //    vgo.id_map_go = 103;
            //    vgo.x_new = 282;
            //    vgo.y_new = 186;
            //    conn.p.change_map(conn.p, vgo);

           //     break;
          //  }
            case 6: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                send_menu_select(conn, 601, new String[]{"Khu Boss Even 0x", "Khu Boss Even 1x", "Khu Boss Even 2x", "Khu Boss Even 7x", "Khu Boss Even 8x", "Khu Boss Even 11x", "Khu Boss Even 13x"});
                break;
            }

            default: {
                Service.send_notice_box(conn, "chức năng đang bảo trì!!");
                break;

            }
        }
    }

    private static void Menu_TachCanh(Session conn, byte index) throws IOException {
        Item3 item = null;
        int count = 0;
        for (int i = 0; i < conn.p.item.bag3.length; i++) {
            Item3 it = conn.p.item.bag3[i];
            if (it != null && it.type == 7 && it.tier > 0) {
                if (count == index) {
                    item = it;
                    break;
                }
                count++;
            }
        }
        if (item != null) {
            conn.p.id_wing_split = index;
            int quant1 = 40;
            int quant2 = 10;
            int quant3 = 50;
            for (int i = 0; i < item.tier; i++) {
                quant1 += GameSrc.wing_upgrade_material_long_khuc_xuong[i];
                quant2 += GameSrc.wing_upgrade_material_kim_loai[i];
                quant3 += GameSrc.wing_upgrade_material_da_cuong_hoa[i];
            }
            if (item.tier > 15) {
                quant1 /= 2;
                quant2 /= 2;
                quant3 /= 2;
            } else {
                quant1 /= 3;
                quant2 /= 3;
                quant3 /= 3;
            }
            Service.send_box_input_yesno(conn, 114, "Bạn có muốn tách cánh này và nhận được: " + quant1
                    + " lông và khúc xương, " + quant2 + " kim loại, " + quant3 + " đá cường hóa?");
        }
    }

    private static void Menu_TienCanh(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                // Service.send_box_UI(conn, index);
                Service.send_msg_data(conn, 23, "create_wings");
                break;
            }
            case 1: {
                Message m2 = new Message(77);
                m2.writer().writeByte(6);
                conn.addmsg(m2);
                m2.cleanup();
                //
                m2 = new Message(77);
                m2.writer().writeByte(1);
                m2.writer().writeUTF("Nâng cấp cánh");
                conn.addmsg(m2);
                m2.cleanup();
                conn.p.is_create_wing = false;
                break;
            }
            //   case 2: {
            //       List<String> list = new ArrayList<>();
            //        for (int i = 0; i < conn.p.item.bag3.length; i++) {
            //            Item3 it = conn.p.item.bag3[i];
            //            if (it != null && it.type == 7) {
            //                list.add(it.name + " +" + it.tier);
            //              }
            //          }
//
            //          String[] list_2 = new String[]{"Trống"};
            //         if (list.size() > 0) {
            //            list_2 = new String[list.size()];
            //             for (int i = 0; i < list_2.length; i++) {
            //                list_2[i] = list.get(i);
            //            }
            //           }
            //           MenuController.send_menu_select(conn, 210, list_2);
//
            //            break;
            //        }
            case 3: {
                conn.p.id_wing_split = -1;
                List<String> list = new ArrayList<>();
                for (int i = 0; i < conn.p.item.bag3.length; i++) {
                    Item3 it = conn.p.item.bag3[i];
                    if (it != null && it.type == 7 && it.tier > 0) {
                        list.add((it.name + " +" + it.tier));
                    }
                }
                if (list.size() > 0) {
                    String[] list_2 = new String[list.size()];
                    for (int i = 0; i < list_2.length; i++) {
                        list_2[i] = list.get(i);
                    }
                    send_menu_select(conn, 121, list_2);
                } else {
                    Service.send_notice_box(conn, "Làm gì có cánh mà đòi tách?");
                }
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_ThayDongCanh_percent(Session conn, byte index) throws IOException {
        if (conn.p.get_ngoc() < 300) {
            Service.send_notice_box(conn, "Không đủ 300 ngọc");
            return;
        }
        conn.p.update_ngoc(-300);
        Log.gI().add_log(conn.p.name, "hết 300 ngọc");
        Item3 it_process = null;
        for (int i = 0; i < conn.p.item.bag3.length; i++) {
            Item3 it = conn.p.item.bag3[i];
            if (it != null && it.type == 7) {
                if (index == 0) {
                    it_process = it;
                    break;
                }
                index--;
            }
        }
        if (it_process != null) {
            Option[] process = new Option[2];
            for (int i = 0; i < it_process.op.size(); i++) {
                if (it_process.op.get(i).id >= 7 && it_process.op.get(i).id <= 11) {
                    if (process[0] == null) {
                        process[0] = it_process.op.get(i);
                    } else if (process[1] == null) {
                        process[1] = it_process.op.get(i);
                    } else {
                        break;
                    }
                }
            }
            if (process[0] != null) {
                process[0].id = (byte) Util.random(7, 12);
                process[0].setParam(Util.random(1500, 2500));
                // process[0].setParam(process[0].getParam(0) + Util.random(50, 100));
            }
            if (process[1] != null) {
                process[1].id = (byte) Util.random(7, 12);
                process[1].setParam(Util.random(1500, 2500));
                //  process[1].setParam(process[1].getParam(0) + Util.random(50, 100));
            }
            Service.send_notice_box(conn, "Thành công");
            conn.p.item.char_inventory(3);
        }
    }

    private static void Menu_Clan_Manager(Session conn, byte index) throws IOException {
        if (conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
            switch (index) {
                case 0: {
                    conn.p.myclan.open_box_clan(conn);
                    break;
                }
                case 1: {
                    if (conn.p.myclan.get_percent_level() >= 100) {
                        Service.send_box_input_yesno(conn, 118,
                                "Bạn có muốn nâng cấp bang lên level " + (conn.p.myclan.level + 1) + " với "
                                + (Clan.vang_upgrade[1] * conn.p.myclan.level) + " vàng và " + (conn.p.myclan.level + 1)
                                + " với " + (Clan.ngoc_upgrade[1] * conn.p.myclan.level) + " ngọc không?");
                    } else {
                        Service.send_notice_box(conn, "Chưa đủ exp để nâng cấp!");
                    }
                    break;
                }
                case 2: {
                    Service.send_box_input_yesno(conn, 116,
                            "Hãy xác nhận việc hủy bang, đừng hối hận khóc lóc xin admin khôi phục lại nhá!");
                    break;
                }
                case 3: {
                    Service.send_box_input_text(conn, 13, "Nhập tên :", new String[]{"Nhập tên :"});
                    break;
                }
                default: {

                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        }
    }

    private static void Menu_Dungeon_Mode_Selection(Session conn, byte index) throws IOException {
//        if (conn.p.dungeon != null && conn.p.dungeon.getWave() == 20) {
//            if (index != 2 && conn.p.party != null && conn.p.party.get_mems().get(0).id != conn.p.id) {
//                Service.send_notice_box(conn, "Chỉ có đội trưởng mới có quyền quyết định!");
//                return;
//            }
//            conn.p.dungeon.setMode(index);
//            if (conn.p.dungeon != null) {
//                conn.p.dungeon.setWave(21);
//                conn.p.dungeon.state = 1;
//            } else {
//                Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử chọn lại!");
//            }
//        }
    }

    private static void Menu_LinhCanh(Session conn, byte index) throws IOException {
//        if (conn.p.dungeon != null && conn.p.dungeon.getWave() == 20) {
//            if (index != 2 && conn.p.party != null && conn.p.party.get_mems().get(0).id != conn.p.id) {
//                Service.send_notice_box(conn, "Chỉ có đội trưởng mới có quyền quyết định!");
//                return;
//            }
//            switch (index) {
//                case 0: {
//                    send_menu_select(conn, 123, new String[]{"Easy", "Normal", "Hard", "Nightmare", "Hell"});
//                    break;
//                }
//                case 1: {
//                    if (conn.p.dungeon != null) {
//                        conn.p.dungeon.state = 6;
//                    } else {
//                        Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử chọn lại!");
//                    }
//                    Service.send_notice_box(conn, "gà thật, éo dám đi tiếp à");
//                    break;
//                }
//                case 2: {
//                    Service.send_notice_box(conn,
//                            "Đến được đây quả là có cố gắng, hãy nói chuyện với phó chỉ huy để nhận thưởng hoàn thành phó bản, hoặc chọn tiếp tục chinh phục để có thể nhận được nhiều phần thưởng hơn");
//                    break;
//                }
//                default: {
//                    Service.send_notice_box(conn, "Chưa có chức năng");
//                    break;
//                }
//            }
//        }
    }

    private static void Menu_PhoChiHuy(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.level < 30) {
                    Service.send_notice_box(conn, "Đạt level 30 mới có thể vào phó bản");
                    return;
                }
                if (conn.p.point_active[0] < 1) {
                    Service.send_notice_box(conn, "Đã hết lượt đi, hãy quay lại vào ngày mai");
                    return;
                }
                String notice = "Danh sách người chơi vào phó bản :\n1) " + conn.p.name + " : level " + conn.p.level;
                if (conn.p.party != null) {
                    Service.send_notice_box(conn, "Phó bản hiện tại chỉ hỗ trợ chế độ solo");
                    return;
                }
                notice += "\nĐộ khó: ??? \nHãy xác nhận." + (conn.p.point_active[0] != 10 ? " phí vào là 30 ngọc." : "");
//                conn.p.dungeon = null;
                Service.send_box_input_yesno(conn, 119, notice);
                break;
            }
            case 1: {
                Service.send_notice_box(conn,
                        "Ngã tư tử thần nâng cấp:\nSau khi vượt qua 20 ải đầu sẽ nhận phần thưởng hoàn thành phó bản, sau đó hãy nói chuyện với npc trong phó bản để quyết định độ khó, cuối cùng là tiếp tục chinh phục phó bản.\n Càng tích nhiều điểm càng được nhiều phần thưởng nhé, điểm sẽ được tính dựa trên số ải vượt qua và lượng dame gây ra.\nLưu ý mỗi ngày đi tối đa 10 lần.");
                break;
            }
            case 2: {
                synchronized (Dungeon.bxh_time_complete) {
                    String notice;
                    if (Dungeon.bxh_time_complete.size() > 0) {
                        notice = "BXH thời gian hoàn thành:\n";
                        int dem = 1;
                        for (Dungeon.BXH_Dungeon_Finished set : Dungeon.bxh_time_complete) {
                            notice += (dem++) + ". " + set.name + " : " + set.time + "s\n";
                        }
                    } else {
                        notice = "Chưa có thông tin";
                    }
                    Service.send_notice_box(conn, notice);
                }
                break;
            }
            case 3: {
                ChiemThanhManager.ClanRegister(conn.p);
                break;
            }
            case 4: {
                if (ChiemThanhManager.timeAttack < System.currentTimeMillis()) {
                    Service.send_notice_box(conn, "Chiếm thành đã kết thúc.");
                } else if (!ChiemThanhManager.joinMap(conn.p)) {
                    Service.send_notice_box(conn, "Bạn không đủ điều kiện tham gia chiếm thành.");
                }
                break;
            }
            case 5: {
                Service.send_notice_box(conn, "Thông tin:\nSố lần còn lại " + conn.p.point_active[0]
                        + ": lần\nTổng điểm hôm nay : " + conn.p.point_active[1]);
                break;
            }
            case 6: {
                if (conn.p.point_active[1] < 1) {
                    Service.send_notice_box(conn, "Hôm nay chưa làm gì cả, không làm mà đòi có ăn à con??");
                    return;
                }
                if (conn.p.item.get_bag_able() < 3) {
                    Service.send_notice_box(conn, "Hành trang không đủ chỗ!");
                    return;
                }
                while (conn.p.point_active[1] > 0) {
                    conn.p.point_active[1]--;
                    short id_ = Medal_Material.m_blue[Util.random(0, 10)];
                    if (conn.p.item.get_bag_able() <= 0) {
                        Service.send_notice_box(conn, "Hành trang không đủ chỗ!");
                        conn.p.item.char_inventory(7);
                        conn.p.update_vang(Util.random(10, 50));
                        return;
                    }
                    if (25 > Util.random(0, 100) && ((conn.p.item.get_bag_able() > 0))) {
                        Item47 itbag = new Item47();
                        itbag.id = id_;
                        itbag.quantity = (short) Util.random(0, 2);
                        itbag.category = 7;
                        conn.p.item.add_item_bag47(7, itbag);
                    }
                    //
                    conn.p.update_vang(Util.random(10, 50));
                }
                conn.p.item.char_inventory(7);
                Service.send_notice_box(conn, "Nhận thành công");
                break;
            }
            case 7: {
                Item3 itbag = new Item3();
                itbag.id = 3596;
                itbag.clazz = ItemTemplate3.item.get(3596).getClazz();
                itbag.type = ItemTemplate3.item.get(3596).getType();
                itbag.level = ItemTemplate3.item.get(3596).getLevel();
                itbag.icon = ItemTemplate3.item.get(3596).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(3596).getOp());
                itbag.color = 5;
                itbag.part = ItemTemplate3.item.get(3596).getPart();
                itbag.tier = 0;
                itbag.islock = true;
                itbag.time_use = 0;
                // thao do
                if (conn.p.item.wear[11] != null && conn.p.item.wear[11].id != 3593 && conn.p.item.wear[11].id != 3599
                        && conn.p.item.wear[11].id != 3596) {
                    Item3 buffer = conn.p.item.wear[11];
                    conn.p.item.wear[11] = null;
                    conn.p.item.add_item_bag3(buffer);
                }
                itbag.name = ItemTemplate3.item.get(3596).getName() + " [Khóa]";
                itbag.UpdateName();
                conn.p.item.wear[11] = itbag;
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(3);
                conn.p.fashion = Part_fashion.get_part(conn.p);
                conn.p.change_map_di_buon(conn.p);
                Service.send_notice_box(conn, "Nhận thành công");
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Pet_Manager(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 21);
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 22);
                break;
            }
            case 2: {
                Service.send_box_UI(conn, 23);
                break;
            }
            case 3: {
                if (conn.p.pet_follow != -1) {
                    for (Pet temp : conn.p.mypet) {
                        if (temp.is_follow) {
                            temp.is_follow = false;
                            Message m = new Message(44);
                            m.writer().writeByte(28);
                            m.writer().writeByte(1);
                            m.writer().writeByte(9);
                            m.writer().writeByte(9);
                            m.writer().writeUTF(temp.name);
                            m.writer().writeByte(temp.type);
                            m.writer().writeShort(conn.p.mypet.indexOf(temp)); // id
                            m.writer().writeShort(temp.level);
                            m.writer().writeShort(temp.getlevelpercent()); // exp
                            m.writer().writeByte(temp.type);
                            m.writer().writeByte(temp.icon);
                            m.writer().writeByte(temp.nframe);
                            m.writer().writeByte(temp.color);
                            m.writer().writeInt(temp.get_age());
                            m.writer().writeShort(temp.grown);
                            m.writer().writeShort(temp.maxgrown);
                            m.writer().writeShort(temp.point1);
                            m.writer().writeShort(temp.point2);
                            m.writer().writeShort(temp.point3);
                            m.writer().writeShort(temp.point4);
                            m.writer().writeShort(temp.maxpoint);
                            m.writer().writeByte(temp.op.size());
                            for (int i2 = 0; i2 < temp.op.size(); i2++) {
                                Option_pet temp2 = temp.op.get(i2);
                                m.writer().writeByte(temp2.id);
                                m.writer().writeInt(temp2.param);
                                m.writer().writeInt(temp2.maxdam);
                            }
                            conn.p.conn.addmsg(m);
                            m.cleanup();
                            break;
                        }
                    }
                    conn.p.pet_follow = -1;
                    Service.send_wear(conn.p);
                    Service.send_char_main_in4(conn.p);
                } else {
                    Service.send_notice_box(conn, "Đã đeo pet đâu mà đòi tháo??");
                }
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }
 

    private static void Menu_Mr_Edgar(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.list_enemies.size() > 0) {
                    String[] name = new String[conn.p.list_enemies.size()];
                    for (int i = 0; i < name.length; i++) {
                        name[i] = conn.p.list_enemies.get(name.length - i - 1);
                    }
                    send_menu_select(conn, 124, name);
                } else {
                    Service.send_notice_box(conn, "Danh sách chưa có ai, hãy đi cà khịa để tạo thêm!");
                }
                break;
            }
            case 1: {
                Service.send_notice_box(conn,
                        "Bị người chơi khác pk thì sẽ được lưu vào danh sách, "
                        + "mỗi lần trả thù sẽ được đưa tới nơi kẻ thù đang đứng với chi phí chỉ vỏn vẹn 2 ngọc.\n"
                        + "Sau khi được đưa tới nơi, tên kẻ thù sẽ được loại ra khỏi danh sách");
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Zoro(Session conn, byte index) throws IOException {
        if (conn.p.myclan != null) {
            if (conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
                switch (index) {
                    case 0: {
                        send_menu_select(conn, 122,
                                new String[]{"Kho bang", "Nâng cấp bang", "Hủy bang hội", "Chuyển thủ lĩnh"});
                        break;
                    }
                    case 1: { //
                        Service.send_box_UI(conn, 29);
                        break;
                    }
                    case 2: {
                        Service.send_box_UI(conn, 30);
                        break;
                    }
                    default: {
                        Service.send_notice_box(conn, "Chưa có chức năng");
                        break;
                    }
                }
            } else {
                switch (index) {
                    case 0: {
                        conn.p.myclan.open_box_clan(conn);
                        break;
                    }
                    case 1: {
                        Service.send_box_input_text(conn, 8, "Góp vàng", new String[]{"Số lượng :"});
                        break;
                    }
                    case 2: {
                        Service.send_box_input_text(conn, 9, "Góp Ngọc", new String[]{"Số lượng :"});
                        break;
                    }
                    case 3: {
                        Service.send_box_input_yesno(conn, 117,
                                "Hãy xác nhận việc rời bang, có khi đi rồi quay lại éo đc đâu nhá");
                        break;
                    }
                    default: {
                        Service.send_notice_box(conn, "Chưa có chức năng");
                        break;
                    }
                }
            }
        } else {
            switch (index) {
                case 0: {
                    Service.send_box_input_yesno(conn, 70, "Bạn có muốn đăng ký tạo bang với phí là 20.000 ngọc");
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        }
    }

    private static void Menu_Benjamin(Session conn, byte index) throws IOException {
        if (conn.p.myclan != null) {
            if (conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
                switch (index) {
                    case 0: {
                        send_menu_select(conn, 122,
                                new String[]{"Kho bang", "Nâng cấp bang", "Hủy bang hội", "Chuyển thủ lĩnh"});
                        break;
                    }
                    case 1: { //
                        Service.send_box_UI(conn, 29);
                        break;
                    }
                    case 2: {
                        Service.send_box_UI(conn, 30);
                        break;
                    }
                    default: {
                        Service.send_notice_box(conn, "Chưa có chức năng");
                        break;
                    }
                }
            } else {
                switch (index) {
                    case 0: {
                        conn.p.myclan.open_box_clan(conn);
                        break;
                    }
                    case 1: {
                        Service.send_box_input_text(conn, 8, "Góp vàng", new String[]{"Số lượng :"});
                        break;
                    }
                    case 2: {
                        Service.send_box_input_text(conn, 9, "Góp Ngọc", new String[]{"Số lượng :"});
                        break;
                    }
                    case 3: {
                        Service.send_box_input_yesno(conn, 117,
                                "Hãy xác nhận việc rời bang, có khi đi rồi quay lại éo đc đâu nhá");
                        break;
                    }
                    default: {
                        Service.send_notice_box(conn, "Chưa có chức năng");
                        break;
                    }
                }
            }
        } else {
            switch (index) {
                case 0: {
                    Service.send_box_input_yesno(conn, 70, "Bạn có muốn đăng ký tạo bang với phí là 20.000 ngọc");
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        }
    }

    private static void Menu_VXMM(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Manager.gI().vxmm.send_in4(conn.p);
                break;
            }
            case 1: {
                Service.send_box_input_text(conn, 3, "Vòng xoay vàng", new String[]{"Tham gia (tối thiểu 10k) :"});
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    

    private static void Menu_VXKC(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Manager.gI().vxkc.send_in4(conn.p);
                break;
            }
            case 1: {
                Service.send_box_input_text(conn, 17, "Vòng xoay ngọc", new String[]{"Tham gia (tối thiểu 500) :"});
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_CuopBien(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                send_menu_select(conn, 125, new String[]{"Xem thông tin", "Tham gia"});
                break;
            }
            case 1: {
                send_menu_select(conn, 132, new String[]{"Xem thông tin", "Tham gia"});
                //Service.send_notice_box(conn, "Sắp ra mắt");
                break;
            }
            case 2: {
                Service.send_notice_box(conn, "Sắp ra mắt");
                break;
            }
               case 3: {
                send_menu_select(conn, 113, new String[]{"Tham gia", "Kết quả", "Lịch Sử"});
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    public static void send_menu_select(Session conn, int idnpc, String[] menu) throws IOException {
        if (!conn.p.isdie) {
            if (menu != null && menu.length > 0) {
                Message m2 = new Message(-30);
                m2.writer().writeShort(idnpc);
                m2.writer().writeByte(0);
                m2.writer().writeByte(menu.length);
                for (int i = 0; i < menu.length; i++) {
                    m2.writer().writeUTF(menu[i]);
                }
                if (conn.ac_admin > 0) {
                    m2.writer().writeUTF("MENU : " + idnpc);
                } else {
                    m2.writer().writeUTF("MENU");
                }
                conn.addmsg(m2);
                m2.cleanup();
            }
        }
    }

    public static void send_menu_select(Session conn, int idnpc, String[] menu, byte idmenu) throws IOException {
        if (!conn.p.isdie) {
            if (menu != null && menu.length > 0) {
                Message m2 = new Message(-30);
                m2.writer().writeShort(idnpc);
                m2.writer().writeByte(idmenu);
                m2.writer().writeByte(menu.length);
                for (int i = 0; i < menu.length; i++) {
                    m2.writer().writeUTF(menu[i]);
                }
                if (conn.ac_admin > 0) {
                    m2.writer().writeUTF("MENU : " + idnpc);
                } else {
                    m2.writer().writeUTF("MENU");
                }
                conn.addmsg(m2);
                m2.cleanup();
            }
        }
    }

    private static void Menu_Aman(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Message m = new Message(23);
                m.writer().writeUTF("Rương đồ");
                m.writer().writeByte(3);
                m.writer().writeShort(0);
                conn.addmsg(m);
                m.cleanup();
                break;
            }
            case 1: {
                if (conn.p.maxbag >= 126) {
                    Service.send_notice_box(conn, "Đã mở rương rồi!");
                    return;
                }
                try ( Connection connection = SQL.gI().getConnection();  Statement statement = connection.createStatement();) {
                    if (statement.executeUpdate("UPDATE `player` SET `maxbag` = 126 WHERE `id` = " + conn.p.index + ";") > 0) {
                        connection.commit();
                    }
                    Service.send_notice_box(conn, "Mở thành công 126 ô, hãy thoát game vào lại để cập nhật!");
                } catch (SQLException e) {
                    e.printStackTrace();
                    //     Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử lại!");
                }
                break;
            }
            case 2: {
                // if (conn.p.update_coin(-25_000)) {
                // if (true) {
                // String js =
                // "[1,%s,%s,%s,%s,10,100,300,1,1,1,1,100,[[0,%s,%s],[23,%s,0],[24,%s,0],[25,%s,0],[26,%s,0]]]";
                // byte[] type = new byte[] {8, 13, 10, 11, 11, 11, 12, 12, 12, 9, 7, 7, 7, 6, 5, 5, 5, 4, 4, 4, 3,
                // 3, 3, 2,
                // 2, 2, 1, 1, 1, 0, 0, 0};
                // byte[] icon = new byte[] {26, 41, 32, 33, 34, 35, 36, 37, 38, 29, 21, 22, 23, 20, 15, 16, 17, 12,
                // 13, 14,
                // 9, 10, 11, 6, 7, 8, 0, 1, 2, 3, 4, 5};
                // int rd = Util.random(0, 32);
                // js = String.format(js, type[rd], icon[rd], 3, 0, Util.random(0, 99), Util.random(99, 999),
                // Util.random(1, 50), Util.random(1, 50), Util.random(1, 50), Util.random(1, 50));
                // if (conn.p.mypet == null) {
                // conn.p.mypet = new Pet(conn.p);
                // }
                // conn.p.mypet.setup((JSONArray) JSONValue.parse(js));
                // Service.send_wear(conn.p);
                // Service.send_char_main_in4(conn.p);
                // conn.p.map.update_in4_inside(conn.p);
                // Service.send_box_notice(conn, "Nhận thành công!");
                // // } else {
                // // Service.send_box_notice(conn, "Éo đủ coin, đi nạp đi!");
                // }
                if (conn.user.contains("knightauto_hsr_")) {
                    if (conn.p.level < 10) {
                        Service.send_notice_box(conn, "Đạt level 10 mới có thể đăng ký tài khoản");
                        return;
                    }
                    Service.send_box_input_text(conn, 6, "Nhập thông tin",
                            new String[]{"Tên đăng nhập mới", "Mật khẩu mới"});
                } else {
                    Service.send_notice_box(conn, "Chức năng đang phát triển...");
                }
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Black_Eye(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 13);
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 14);
                break;
            }
            case 2: {
                Service.send_box_UI(conn, 15);
                break;
            }
            case 3: {
                Service.send_box_UI(conn, 16);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_BXH(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                BXH.send(conn, 0);
                break;
            }
            case 1: {
                send_menu_select(conn, 888, new String[]{"Bang Giàu Có Nhất", "Bang Hùng Mạnh Nhất", "Bang Châu Báu Nhất", "Top Bang Chiếm Mỏ"});

                break;
            }
            case 2: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
            }

            send_menu_select(conn, 345, new String[]{"Ngọc 1 Sao", "Ngọc 2 Sao", "Ngọc 3 Sao", "Ngọc 4 Sao", "Ngọc 5 Sao", "Ngọc 6 Sao", "Ngọc 7 Sao", "Hướng Đẫn Sự Kiện MiNi","Map Diệt Ma"});
            break;
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;

            }
        }
    }

    private static void Menu_BXHCLAN(Session conn, byte index) throws IOException {
        switch (index) {
            case 1: {
                String[] list = new String[Math.min(20, BXH.BXH_clan.size())];
                for (int i = 0; i < list.length; i++) {
                    list[i] = ("Hạng " + (i + 1) + " : "
                            + BXH.BXH_clan.get(i).name_clan
                            + " - " + BXH.BXH_clan.get(i).name_clan_shorted
                            + "\n Vàng : " + BXH.BXH_clan.get(i).vang) + " - "
                           ;

                }
                if (list.length > 0) {
                    send_menu_select(conn, 120, list);
                } else {
                    Service.send_notice_box(conn, "Chưa có thong tin");
                }

                break;
            }
          
            default: {
                Service.send_notice_box(conn, "Chức Năng Đang Được Bảo Trì");
                break;
            }
        }
    }

    private static void Menu_Miss_Anna(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Service.send_box_input_text(conn, 0, "Nhập mã code", new String[]{"Code"});
                break;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {
                if (conn.p.item.wear[index + 9] == null) {
                    Service.send_notice_box(conn, "Mặc đâu mà tháo");
                } else if (conn.p.item.get_bag_able() > 0) {
                    Item3 buffer = conn.p.item.wear[index + 9];
                    conn.p.item.wear[index + 9] = null;
                    if (buffer.id != 3599 && buffer.id != 3593 && buffer.id != 3596) {
                        conn.p.item.add_item_bag3(buffer);
                    }
                    conn.p.item.char_inventory(3);
                    conn.p.fashion = Part_fashion.get_part(conn.p);
                    Service.send_wear(conn.p);
                    Service.send_char_main_in4(conn.p);
                    MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                    Service.send_notice_box(conn, "Tháo thành công");
                    //
                    if (index == 2 && conn.p.pet_di_buon != null) {
                        Message mout = new Message(8);
                        mout.writer().writeShort(conn.p.pet_di_buon.index);
                        for (int i = 0; i < conn.p.map.players.size(); i++) {
                            Player p0 = conn.p.map.players.get(i);
                            if (p0 != null) {
                                p0.conn.addmsg(mout);
                            }
                        }
                        mout.cleanup();
                        //
                        Pet_di_buon_manager.remove(conn.p.pet_di_buon.name);
                        conn.p.pet_di_buon = null;
                    }
                } else {
                    Service.send_notice_box(conn, "Hành trang đầy!");
                }
                break;
            }
            case 10: {
                // nhận quà top lv
                try ( Connection connection = SQL.gI().getConnection();  Statement st = connection.createStatement();  Statement ps = connection.createStatement();  ResultSet rs = st.executeQuery("SELECT * FROM `giftcode2` WHERE `name_player` = '" + conn.p.name + "' AND `type_gift` = '2' AND `status` = '0';")) {
                    byte empty_box = (byte) 0;
                    if (!rs.next()) {
                        Service.send_notice_box(conn, "Không tìm thấy đơn hàng, hoặc bạn đã nhận trước đó!");
                        return;
                    }
                    String mess = rs.getString("logger");
                    empty_box = rs.getByte("empty_box");
                    if (conn.p.item.get_bag_able() >= empty_box) {
                        if (ps.executeUpdate("UPDATE `giftcode2` SET `status` = '1' WHERE `id` = '" + rs.getInt("id") + "';") > 0) {
                            connection.commit();
                        }
                        JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3_defauft"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            if (jsar2 == null || jsar2.size() < 1) {
                                continue;
                            }
                            Item3 itbag = new Item3();
                            short it = Short.parseShort(jsar2.get(0).toString());
                            short date = Short.parseShort(jsar2.get(1).toString());
                            itbag.id = it;
                            itbag.name = ItemTemplate3.item.get(it).getName();
                            itbag.clazz = ItemTemplate3.item.get(it).getClazz();
                            itbag.type = ItemTemplate3.item.get(it).getType();
                            itbag.level = ItemTemplate3.item.get(it).getLevel();
                            itbag.icon = ItemTemplate3.item.get(it).getIcon();
                            itbag.op = new ArrayList<>();
                            itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
                            itbag.color = ItemTemplate3.item.get(it).getColor();
                            itbag.part = ItemTemplate3.item.get(it).getPart();
                            itbag.tier = 0;
                            itbag.time_use = 0;
                            itbag.islock = false;
                            if (date > 0) {
                           //     itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
                            }
                            conn.p.item.add_item_bag3(itbag);
                        }
                        jsar.clear();
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
                        if (jsar != null && jsar.size() > 0) {
                            for (int i = 0; i < jsar.size(); i++) {
                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                                Item3 temp = new Item3();
                                temp.id = Short.parseShort(jsar2.get(0).toString());
                                temp.clazz = Byte.parseByte(jsar2.get(1).toString());
                                temp.type = Byte.parseByte(jsar2.get(2).toString());
                                temp.level = Short.parseShort(jsar2.get(3).toString());
                                temp.icon = Short.parseShort(jsar2.get(4).toString());
                                temp.color = Byte.parseByte(jsar2.get(5).toString());
                                temp.part = Byte.parseByte(jsar2.get(6).toString());
                                temp.islock = Byte.parseByte(jsar2.get(7).toString()) == 1;
                                temp.name = ItemTemplate3.item.get(temp.id).getName();
                                if (temp.islock) {
                                    temp.name += " [Khóa]";
                                }
                                temp.tier = Byte.parseByte(jsar2.get(8).toString());
                                // if (temp.type == 15) {
                                // temp.tier = 0;
                                // }
                                JSONArray jsar3 = (JSONArray) JSONValue.parse(jsar2.get(9).toString());
                                temp.op = new ArrayList<>();
                                for (int j = 0; j < jsar3.size(); j++) {
                                    JSONArray jsar4 = (JSONArray) JSONValue.parse(jsar3.get(j).toString());
                                    temp.op.add(
                                            new Option(Byte.parseByte(jsar4.get(0).toString()), Integer.parseInt(jsar4.get(1).toString()), temp.id));
                                }
                                temp.time_use = 0;
                                if (jsar2.size() >= 11) {
                                    temp.time_use = Long.parseLong(jsar2.get(10).toString());
                                }
                                if (jsar2.size() >= 12) {
                                    temp.tierStar = Byte.parseByte(jsar2.get(11).toString());
                                }
                                if (jsar2.size() >= 13) {
                                    temp.expiry_date = Long.parseLong(jsar2.get(12).toString());
                                }
                                temp.UpdateName();
                                conn.p.item.add_item_bag3(temp);
                            }
                            jsar.clear();
                        }

                        //
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            Item47 itbag = new Item47();
                            itbag.id = Short.parseShort(jsar2.get(0).toString());
                            itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                            itbag.category = 4;
                            conn.p.item.add_item_bag47(4, itbag);
                        }
                        jsar.clear();
                        //
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            Item47 itbag = new Item47();
                            itbag.id = Short.parseShort(jsar2.get(0).toString());
                            itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                            itbag.category = 7;
                            conn.p.item.add_item_bag47(7, itbag);

                        }
                        jsar.clear();
                        conn.p.update_vang(rs.getLong("vang"));
                        conn.p.update_ngoc(rs.getLong("ngoc"));
                        Log.gI().add_log(conn.p.name, "Get order :" + rs.getInt("id"));
                        conn.p.item.char_inventory(5);
                        conn.p.item.char_inventory(3);
                        conn.p.item.char_inventory(4);
                        conn.p.item.char_inventory(7);

                        Service.send_notice_box(conn, mess);
                    } else {
                        
                        Service.send_notice_box(conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 11: {
                // nhận quà top lv
                try ( Connection connection = SQL.gI().getConnection();  Statement st = connection.createStatement();  Statement ps = connection.createStatement();  ResultSet rs = st.executeQuery("SELECT * FROM `giftcode2` WHERE `name_player` = '" + conn.p.name + "' AND `type_gift` = '1' AND `status` = '0';")) {
                    byte empty_box = (byte) 0;
                    if (!rs.next()) {
                        Service.send_notice_box(conn, "Không tìm thấy phần quà, hoặc bạn đã nhận trước đó!");
                        return;
                    }
                    String mess = rs.getString("logger");
                    empty_box = rs.getByte("empty_box");
                    if (conn.p.item.get_bag_able() >= empty_box) {
                        if (ps.executeUpdate("UPDATE `giftcode2` SET `status` = '1' WHERE `id` = '" + rs.getInt("id") + "';") > 0) {
                            connection.commit();
                        }
                        JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3_defauft"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            if (jsar2 == null || jsar2.size() < 1) {
                                continue;
                            }
                            Item3 itbag = new Item3();
                            short it = Short.parseShort(jsar2.get(0).toString());
                            short date = Short.parseShort(jsar2.get(1).toString());
                            itbag.id = it;
                            itbag.name = ItemTemplate3.item.get(it).getName();
                            itbag.clazz = ItemTemplate3.item.get(it).getClazz();
                            itbag.type = ItemTemplate3.item.get(it).getType();
                            itbag.level = ItemTemplate3.item.get(it).getLevel();
                            itbag.icon = ItemTemplate3.item.get(it).getIcon();
                            itbag.op = new ArrayList<>();
                            itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
                            itbag.color = ItemTemplate3.item.get(it).getColor();
                            itbag.part = ItemTemplate3.item.get(it).getPart();
                            itbag.tier = 0;
                            itbag.time_use = 0;
                            itbag.islock = false;
                            if (date > 0) {
                      //          itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
                            }
                            conn.p.item.add_item_bag3(itbag);
                        }
                        jsar.clear();
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
                        if (jsar != null && jsar.size() > 0) {
                            for (int i = 0; i < jsar.size(); i++) {
                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                                Item3 temp = new Item3();
                                temp.id = Short.parseShort(jsar2.get(0).toString());
                                temp.clazz = Byte.parseByte(jsar2.get(1).toString());
                                temp.type = Byte.parseByte(jsar2.get(2).toString());
                                temp.level = Short.parseShort(jsar2.get(3).toString());
                                temp.icon = Short.parseShort(jsar2.get(4).toString());
                                temp.color = Byte.parseByte(jsar2.get(5).toString());
                                temp.part = Byte.parseByte(jsar2.get(6).toString());
                                temp.islock = Byte.parseByte(jsar2.get(7).toString()) == 1;
                                temp.name = ItemTemplate3.item.get(temp.id).getName();
                                if (temp.islock) {
                                    temp.name += " [Khóa]";
                                }
                                temp.tier = Byte.parseByte(jsar2.get(8).toString());
                                // if (temp.type == 15) {
                                // temp.tier = 0;
                                // }
                                JSONArray jsar3 = (JSONArray) JSONValue.parse(jsar2.get(9).toString());
                                temp.op = new ArrayList<>();
                                for (int j = 0; j < jsar3.size(); j++) {
                                    JSONArray jsar4 = (JSONArray) JSONValue.parse(jsar3.get(j).toString());
                                    temp.op.add(
                                            new Option(Byte.parseByte(jsar4.get(0).toString()), Integer.parseInt(jsar4.get(1).toString()), temp.id));
                                }
                                temp.time_use = 0;
                                if (jsar2.size() >= 11) {
                                    temp.time_use = Long.parseLong(jsar2.get(10).toString());
                                }
                                if (jsar2.size() >= 12) {
                                    temp.tierStar = Byte.parseByte(jsar2.get(11).toString());
                                }
                                if (jsar2.size() >= 13) {
                                    temp.expiry_date = Long.parseLong(jsar2.get(12).toString());
                                }
                                temp.UpdateName();
                                conn.p.item.add_item_bag3(temp);
                            }
                            jsar.clear();
                        }

                        //
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            Item47 itbag = new Item47();
                            itbag.id = Short.parseShort(jsar2.get(0).toString());
                            itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                            itbag.category = 4;
                            conn.p.item.add_item_bag47(4, itbag);
                        }
                        jsar.clear();
                        //
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            Item47 itbag = new Item47();
                            itbag.id = Short.parseShort(jsar2.get(0).toString());
                            itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                            itbag.category = 7;
                            conn.p.item.add_item_bag47(7, itbag);

                        }
                        jsar.clear();
                        conn.p.update_vang(rs.getLong("vang"));
                        conn.p.update_ngoc(rs.getLong("ngoc"));
                        Log.gI().add_log(conn.p.name, "Get order :" + rs.getInt("id"));
                        conn.p.item.char_inventory(5);
                        conn.p.item.char_inventory(3);
                        conn.p.item.char_inventory(4);
                        conn.p.item.char_inventory(7);

                        Service.send_notice_box(conn, mess);
                    } else {
                        Service.send_notice_box(conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            }
        }
    }

    private static void Menu_Doiaochoang(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                send_menu_select(conn, 346, new String[]{"Áo Choàng Nam Tước", "Áo Choàng Tử Tước ", "Áo Choàng Bá Tước ", "Áo Choàng Hầu Tước ", "Áo Choàng Công Tước"});
                break;
            }
            case 1: {
                send_menu_select(conn, 347, new String[]{"Áo Choàng Nam Tước", "Áo Choàng Tử Tước ", "Áo Choàng Bá Tước ", "Áo Choàng Hầu Tước ", "Áo Choàng Công Tước"});
                break;
            }
            case 2: {
                send_menu_select(conn, 348, new String[]{"Áo Choàng Nam Tước", "Áo Choàng Tử Tước ", "Áo Choàng Bá Tước ", "Áo Choàng Hầu Tước ", "Áo Choàng Công Tước"});
                break;
            }
            case 3: {
                send_menu_select(conn, 349, new String[]{"Áo Choàng Nam Tước", "Áo Choàng Tử Tước ", "Áo Choàng Bá Tước ", "Áo Choàng Hầu Tước ", "Áo Choàng Công Tước"});
                break;
            }
            case 4: {
                send_menu_select(conn, 350, new String[]{"Áo Choàng Nam Tước", "Áo Choàng Tử Tước ", "Áo Choàng Bá Tước ", "Áo Choàng Hầu Tước ", "Áo Choàng Công Tước"});
                break;
            }
            case 5: {
                send_menu_select(conn, 351, new String[]{"Áo Choàng Nam Tước", "Áo Choàng Tử Tước ", "Áo Choàng Bá Tước ", "Áo Choàng Hầu Tước ", "Áo Choàng Công Tước"});
                break;
            }
            case 6: {
                send_menu_select(conn, 777, new String[]{"Áo Choàng Nam Tước", "Áo Choàng Tử Tước ", "Áo Choàng Bá Tước ", "Áo Choàng Hầu Tước ", "Áo Choàng Công Tước"});
                break;
            }
            case 7: {

                String s = "💖Hướng Dẫn💖";
                s += "\nCó 2 cách săn ngọc rồng";
                s += "\nNgười chơi phải từ level 40 trở lên";
                s += "\nCách chơi :";
                s += "\nNgọc rồng sẽ đc random tại 3 map {Bìa Rừng ,Hang Lửa ,Rừng Ảo Giác} .";
                s += "\nNgoài ra các hiệp sĩ cũng sẽ đuọc săn boss ở map riêng tại npc bxh";
                s += "\nNhặc được ngọc các hiệp sĩ có thể đổi quà tại npc bxh";
                s +="\nKết thúc Even Trân Trọng Cảm Ơn Các Hiệp Sĩ Đã Ủng hộ Sever";
                s += "\nBy HSO!!!";
                    
                      
                break;
            }
             case 8: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 116;
                vgo.x_new = 1020;
                vgo.y_new = 588;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Doiaochoang7(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 470) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 7 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 470, 1);
                short iditem = 4676;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 1: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 470) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 7 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 470, 1);
                short iditem = 4679;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 2: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 470) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 1 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 470, 1);
                short iditem = 4682;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 3: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 470) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 7 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 470, 1);
                short iditem = 4685;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 4: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 470) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 7 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 470, 1);
                short iditem = 4688;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Doiaochoang1(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 464) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 1 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 464, 1);
                short iditem = 4676;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 1;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 1: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 464) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 1 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 464, 1);
                short iditem = 4679;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 1;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 2: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 464) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 1 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 464, 1);
                short iditem = 4682;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 1;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 3: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 464) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 1 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 464, 1);
                short iditem = 4685;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 1;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 4: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 464) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 1 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 464, 1);
                short iditem = 4688;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 1;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Doiaochoang2(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 465) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 2 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 465, 1);
                short iditem = 4676;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 2;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 1: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 465) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 2 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 465, 1);
                short iditem = 4679;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 2;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 2: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 465) < 1) {
                    Service.send_notice_box(conn, "thiếu  ngọc 2 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 465, 1);
                short iditem = 4682;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 2;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 3: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 465) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 2 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 465, 1);
                short iditem = 4685;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 2;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 4: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 465) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 2 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 465, 1);
                short iditem = 4688;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 2;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Doiaochoang3(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 466) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 3 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 466, 1);
                short iditem = 4676;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 3;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 1: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 466) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 3 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 466, 1);
                short iditem = 4679;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 3;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 2: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 466) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 3 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 466, 1);
                short iditem = 4682;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 3;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 3: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 466) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 3 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 466, 1);
                short iditem = 4685;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 3;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 4: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 466) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 3 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 466, 1);
                short iditem = 4688;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 3;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Doiaochoang4(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 467) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 4 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 467, 1);
                short iditem = 4676;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 4;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 1: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 467) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 4 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 467, 1);
                short iditem = 4679;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 4;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 2: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 467) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 4 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 467, 1);
                short iditem = 4682;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 4;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 3: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 467) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 4 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 467, 1);
                short iditem = 4685;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 4;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 4: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 467) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 4 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 467, 1);
                short iditem = 4688;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 4;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Doiaochoang5(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 468) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 5 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 468, 1);
                short iditem = 4676;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 5;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 1: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 468) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 5 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 468, 1);
                short iditem = 4679;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 5;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 2: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 468) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 5 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 468, 1);
                short iditem = 4682;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 5;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 3: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 468) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 5 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 468, 1);
                short iditem = 4685;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 5;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 4: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 468) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 5 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 468, 1);
                short iditem = 4688;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 5;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Doiaochoang6(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 469) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 6 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 469, 1);
                short iditem = 4676;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 6;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 1: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 469) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 6 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 469, 1);
                short iditem = 4679;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 6;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 2: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 469) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 6 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 469, 1);
                short iditem = 4682;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 6;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 3: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 469) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 6 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 469, 1);
                short iditem = 4685;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 6;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 4: {
                if (conn.p.get_ngoc() < 0 || conn.p.item.total_item_by_id(7, 465) < 1) {
                    Service.send_notice_box(conn, "thiếu 1 ngọc 6 sao!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-0);
                conn.p.item.remove(7, 469, 1);
                short iditem = 4688;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 6;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_PhapSu(Session conn, byte index) throws IOException {
        conn.p.ResetCreateItemStar();
        switch (index) {
            case 0: {
                conn.p.id_item_rebuild = -1;
                conn.p.is_use_mayman = false;
                conn.p.id_use_mayman = -1;
                Service.send_box_UI(conn, 18);
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 17);
                break;
            }
            case 2: {
                conn.p.item_replace = -1;
                conn.p.item_replace2 = -1;
                Service.send_box_UI(conn, 19);
                break;
            }
            case 3: {
                Service.send_box_UI(conn, 24);
                break;
            }
            case 4: {
                Service.send_box_UI(conn, 25);
                conn.p.ResetCreateItemStar();
                conn.p.id_medal_is_created = 0;
                break;
            }
            case 5: {
                Service.send_box_UI(conn, 26);
                conn.p.ResetCreateItemStar();
                conn.p.id_medal_is_created = 1;
                break;
            }
            case 6: {
                Service.send_box_UI(conn, 27);
                conn.p.ResetCreateItemStar();
                conn.p.id_medal_is_created = 2;
                break;
            }
            case 7: {
                Service.send_box_UI(conn, 28);
                conn.p.ResetCreateItemStar();
                conn.p.id_medal_is_created = 3;
                break;
            }
            case 8: {
                conn.p.ResetCreateItemStar();
                Service.send_box_UI(conn, 33);
                break;
            }
            case 9:
            case 10: {
                ArrayList<String> myList = new ArrayList<String>();
                //Item3[] item3 = conn.p.item.bag3;
                Item3[] itemw = conn.p.item.wear;

                if (itemw == null || itemw.length < 13) {
                    Service.send_notice_box(conn, "Lỗi hành trang hoặc trang bị!");
                    return;
                }
                if (itemw.length > 12 && itemw[12] != null && Helps.CheckItem.isMeDay(itemw[12].id)) {
                    myList.add(itemw[12].name + " (1000 ngọc)");
                }
                if (myList == null || myList.size() <= 0) {
                    Service.send_notice_box(conn, "Không có vật phẩm phù hợp!");
                    return;
                }

                send_menu_select(conn, index == 9 ? 4 : 5, myList.toArray(new String[0]));

                //Service.send_notice_box(conn, "Chưa có chức năng"); doi dong st
                break;
            }
//            case 10: {
//                Service.send_notice_box(conn, "Chưa có chức năng"); doi pt st
//                break;
//            }
            case 11: {
                Service.send_box_UI(conn, 34);
                break;
            }
            case 12: {
                Service.send_box_UI(conn, 35);
                break;
            }
            case 13: {
                Service.send_box_UI(conn, 36);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Admin(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn chưa đủ quyền để thực hiện!");
                    return;
                }
                Service.send_box_input_yesno(conn, 88, "Bạn có chắc chắn muốn bảo trì server?");
                break;
            }
            case 1: {
                if (conn.ac_admin <= 3) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                conn.p.update_vang(1_000_000_000);
                //conn.p.item.char_inventory(5);
                Service.send_notice_nobox_white(conn, "+ 1.000.000.000 vàng");
                break;
            }
            case 2: {
                if (conn.ac_admin <= 3) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                conn.p.update_ngoc(1_000_000);
                //conn.p.item.char_inventory(5);
                Service.send_notice_nobox_white(conn, "+ 1.000.000 ngọc");
                break;
            }
            case 3: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                SaveData.process();
                Service.send_notice_nobox_white(conn, "data đã đc cập nhật");
                break;
            }
            case 4: {
                Service.send_box_input_text(conn, 1, "Get Item",
                        new String[]{"Nhập loại (3,4,7) vật phẩm :", "Nhập id vật phẩm", "Nhập số lượng"});
                break;
            }
            case 5: {
                Service.send_box_input_text(conn, 2, "Plus Level", new String[]{"Nhập level :"});
                break;
            }
            case 6: {
                Service.send_box_input_text(conn, 4, "Set Xp", new String[]{"Nhập mức x :"});
                break;
            }
            case 7: {
                Service.send_box_input_text(conn, 18, "Tên nhân vật", new String[]{"Nhập Tên nhân vật :"});
                break;
            }
            case 8: {
                Service.send_box_input_text(conn, 19, "Tên nhân vật", new String[]{"Nhập Tên nhân vật :"});
                break;
            }
            case 9: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.isLockVX = !Manager.isLockVX;
                Service.send_notice_box(conn, "Vòng xoay vàng ngọc đã " + (Manager.isLockVX ? "khóa" : "mở"));
                //Service.send_box_input_text(conn, 19, "Tên nhân vật", new String[]{"Nhập Tên nhân vật :"});
                break;
            }
            case 10: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.isGiaoDich = !Manager.isGiaoDich;
                Service.send_notice_box(conn, "Giao dịch đã " + (Manager.isGiaoDich ? "mở" : "khóa"));
                break;
            }
            case 11: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.isKmb = !Manager.isKmb;
                Service.send_notice_box(conn, "Giao dịch đã " + (Manager.isKmb ? "mở" : "khóa"));
                break;
            }
            case 12: {
                if (conn.ac_admin < 4) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                for (Pet pet : conn.p.mypet) {
                    if (pet.time_born > 0) {
                        pet.time_born = 3;
                    }
                }
                Service.send_notice_box(conn, "Đã xong");
                break;
            }
            case 13: {
                if (conn.ac_admin < 4) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.BuffAdmin = !Manager.BuffAdmin;
                Service.send_notice_box(conn, "Buff Admin đã: " + (Manager.BuffAdmin ? "Bật" : "Tắt"));
                break;
            }
            case 14: {
                if (conn.ac_admin < 4) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.BuffAdminMaterial = !Manager.BuffAdminMaterial;
                Service.send_notice_box(conn, "Buff nguyên liệu cho Admin Đã: " + (Manager.BuffAdminMaterial ? "Bật" : "Tắt"));
                break;
            }
            case 15: {
                if (conn.ac_admin < 5) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.gI().chiem_mo.mo_open_atk();
                Manager.gI().chatKTGprocess(" Thời gian chiếm mỏ đã đến!");
                break;
            }
            case 16: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.gI().chiem_mo.mo_close_atk();
                Manager.gI().chatKTGprocess(" Thời gian chiếm mỏ đã đóng!");
                break;
            }
            case 17: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                if (!LoiDaiManager.isRegister) {
                    LoiDaiManager.gI().startRegister();
                    Service.send_notice_box(conn, "Đã mở đăng kí ld!");
                } else {
                    LoiDaiManager.timeRegister = System.currentTimeMillis() - 1000;
                    Service.send_notice_box(conn, "Đã đóng đăng kí ld!");
                }
                break;
            }
            case 18: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                if (Manager.gI().event == 2) {
                    ev_he.Event_2.ClearMob();
                    ev_he.Event_2.ResetMob();
                    Service.send_notice_box(conn, "Đã thực hiện reset mob events");
                }
                break;
            }
            case 19: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                if (ChiemThanhManager.isRegister) {
                    ChiemThanhManager.EndRegister();
                } else {
                    ChiemThanhManager.StartRegister();
                }
                Service.send_notice_box(conn, "Đã thực hiện " + (ChiemThanhManager.isRegister ? "mở" : "đóng") + " đăng kí chiếm thành");
                break;
            }
            case 20: {
                Service.send_notice_box(conn, "Chức năng đang được hoàn thiện.");
                break;
            }
            case 21: {
                if (conn.ac_admin < 4) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Service.send_box_input_text(conn, 21, "Dịch chuyển map",
                        new String[]{"Nhập idMap", "Nhập tọa độ x", "Nhập tọa độ y"});
                break;
            }
            case 22: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.gI().load_config();
                break;
            }
            case 23: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.logErrorLogin = !Manager.logErrorLogin;
                Service.send_notice_box(conn, "Bạn đã " + (Manager.logErrorLogin ? "Bật" : "Tắt") + " log error");
                break;
            }
            case 24: {
                Service.send_box_input_text(conn, 24, "Disconnect", new String[]{"Nhập loại :", "Nhập Tên :"});
                break;
            }
            case 25: {
                String ssss = "Start Check \n-----------------------------\n";
                try {
                    Message m = new Message(53);
                    m.writer().writeUTF("check log");
                    m.writer().writeByte(1);
                    int mapnulls = 0;
                    int mapnull = 0;
                    int pnull = 0;
                    ssss += "\nvo ne";
                    for (Map[] map : Map.entrys) {
                        if (map == null) {
                            mapnulls++;
                            continue;
                        }
                        for (Map map0 : map) {
                            if (map0 == null) {
                                mapnull++;
                                continue;
                            }
                            for (int i = 0; i < map0.players.size(); i++) {
                                if (map0.players.get(i) == null || map0.players.get(i).conn == null) {
                                    pnull++;
                                    continue;
                                }
                                map0.players.get(i).conn.addmsg(m);
                            }
                        }
                    }
                    try {
                        for (Group_ld g : LoiDaiManager.gI().Group_entrys) {
                            for (LoiDai2 l : g.ld_entrys) {
                                for (Player p0 : l.map.players) {
                                    if (p0 != null && p0.conn != null && p0.conn.connected) {
                                        p0.conn.addmsg(m);
                                    }
                                }
                            }
                        }
                    } catch (Exception ee) {
                    }
                    ssss += "\n" + mapnulls + " Map[]Null";
                    ssss += "\n" + mapnull + " MapNull";
                    ssss += "\n" + pnull + " PlayerNull";
                    m.cleanup();
                } catch (Exception ex) {
                    Service.send_notice_box(conn, "Lỗi: " + ex.getMessage());
                    ex.printStackTrace();
                    StackTraceElement[] stackTrace = ex.getStackTrace(); // Lấy thông tin ngăn xếp gọi hàm

                    for (StackTraceElement element : stackTrace) {
                        ssss += ("Class: " + element.getClassName());
                        ssss += ("\nMethod: " + element.getMethodName());
                        ssss += ("\nFile: " + element.getFileName());
                        ssss += ("\nLine: " + element.getLineNumber());
                        ssss += ("------------------------\n");
                    }

                }
                Helps.Save_Log.process("checkbug.txt", ssss);
                break;
            }
            case 26: {
                String ssss = "Start Fix \n-----------------------------\n";
                try {
                    Message m = new Message(53);
                    m.writer().writeUTF("check log");
                    m.writer().writeByte(1);
                    int mapnulls = 0;
                    int mapnull = 0;
                    int pnull = 0;
                    ssss += "\nvo ne";
                    for (Map[] map : Map.entrys) {
                        if (map == null) {
                            mapnulls++;
                            continue;
                        }
                        for (Map map0 : map) {
                            if (map0 == null) {
                                mapnull++;
                                continue;
                            }
                            for (int i = map0.players.size() - 1; i >= 0; i--) {
                                if (map0.players.get(i) == null || map0.players.get(i).conn == null) {
                                    map0.players.remove(i);
                                }
                            }
                        }
                    }
                    try {
                        for (Group_ld g : LoiDaiManager.gI().Group_entrys) {
                            for (LoiDai2 l : g.ld_entrys) {
                                for (Player p0 : l.map.players) {
                                    if (p0 != null && p0.conn != null && p0.conn.connected) {
                                        p0.conn.addmsg(m);
                                    }
                                }
                            }
                        }
                    } catch (Exception ee) {
                    }
                    ssss += "\n" + mapnulls + " Map[]Null";
                    ssss += "\n" + mapnull + " MapNull";
                    ssss += "\n" + pnull + " PlayerNull";
                    m.cleanup();
                } catch (Exception ex) {
                    Service.send_notice_box(conn, "Lỗi: " + ex.getMessage());
                    ex.printStackTrace();
                    StackTraceElement[] stackTrace = ex.getStackTrace(); // Lấy thông tin ngăn xếp gọi hàm

                    for (StackTraceElement element : stackTrace) {
                        ssss += ("Class: " + element.getClassName());
                        ssss += ("\nMethod: " + element.getMethodName());
                        ssss += ("\nFile: " + element.getFileName());
                        ssss += ("\nLine: " + element.getLineNumber());
                        ssss += ("------------------------\n");
                    }

                }
                Service.send_notice_box(conn, "xong");
                Helps.Save_Log.process("checkbug.txt", ssss);
                break;
            }
            case 27: {
               if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                    Service.send_box_input_text(conn, 99, "Nhập thông tin",
                    new String[]{"Tên nhân vật", "Số tiền", "Coin"});
            
             break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Zulu(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                switch (conn.p.clazz) {
                    case 0: {
                        Service.send_msg_data(conn, 23, "tocchienbinh");
                        break;
                    }
                    case 1: {
                        Service.send_msg_data(conn, 23, "tocsatthu");
                        break;
                    }
                    case 2:
                    case 3: {
                        Service.send_msg_data(conn, 23, "tocphapsu");
                        break;
                    }
                }
                break;
            }
            case 1: {
                if (conn.p.diemdanh == 1) {
                    conn.p.diemdanh = 0;
                    int ngoc_ = Util.random(100, 1000);
                    conn.p.update_ngoc(ngoc_);
                    Log.gI().add_log(conn.p.name, "Điểm danh ngày được " + Util.number_format(ngoc_) + " ngọc");
                    conn.p.item.char_inventory(5);
                    Service.send_notice_box(conn, "Bạn đã điểm danh thành công, được " + ngoc_ + " ngọc");
                } else {
                    Service.send_notice_box(conn, "Bạn đã điểm danh hôm nay rồi");
                }
                break;
            }
            case 2: {
                Service.send_box_input_text(conn, 5, "Đổi coin sang ngọc", new String[]{"Tỷ lệ 1000 coin = 500 ngọc"});
                break;
            }
            case 3: {
                Service.send_box_input_text(conn, 14, "Đổi coin sang vàng", new String[]{"Tỷ lệ 1000 coin = 5tr vàng"});
                break;
            }

//            case 4: {
//                Service.send_notice_box(conn, "Chức năng không còn tồn tại.");
//                //Service.send_box_input_yesno(conn, 121, "1000 ngọc cho 2h, hãy xác nhận");
//                break;
//            }
//            case 5: {
//                EffTemplate ef = conn.p.get_eff(-126);
//                if (ef != null && ef.time > System.currentTimeMillis()) {
//                    Service.send_notice_box(conn,
//                            "Thời gian còn lại : " + Util.getTime((int) (ef.time - System.currentTimeMillis()) / 1000));
//                } else {
//                    Service.send_notice_box(conn, "Chức năng không còn tồn tại.");
//                    //Service.send_notice_box(conn, "Chưa đăng ký kiểm tra cái gì?");
//                }
//                break;
//            }
            case 4: {
                if (conn.p.type_exp == 0) {
                    conn.p.type_exp = 1;
                    Service.send_notice_box(conn, "Đã bật nhận exp");
                } else {
                    conn.p.type_exp = 0;
                    Service.send_notice_box(conn, "Đã tắt nhận exp");
                }
                break;
            }
            case 5: {
                Service.send_box_input_text(conn, 30, "Đổi mật khẩu", new String[]{"nhập mật khẩu cũ",
                    "nhập mật khẩu mới", "nhập lại mật khẩu mới"});
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_ChangeZone(Session conn) throws IOException {
        Map[] map = Map.get_map_by_id(conn.p.map.map_id);
        if (map != null) {
            Message m = new Message(54);
            if (Map.is_map_cant_save_site(conn.p.map.map_id)) {
                m.writer().writeByte(conn.p.map.maxzone);
            } else {
                m.writer().writeByte(conn.p.map.maxzone + 1);
            }
            //
            for (int i = 0; i < conn.p.map.maxzone; i++) {
                if (map[i].players.size() > (map[i].maxplayer - 2)) {
                    m.writer().writeByte(2); // redzone
                } else if (map[i].players.size() >= (map[i].maxplayer / 2)) {
                    m.writer().writeByte(1); // yellow zone
                } else {
                    m.writer().writeByte(0); // green zone
                }
                if (i == 4 && Map.is_map_chiem_mo(conn.p.map, false)) {
                    m.writer().writeByte(4);
                } else {
                    m.writer().writeByte(0);
                }
            }
            if (!Map.is_map_cant_save_site(conn.p.map.map_id)) {
                m.writer().writeByte(1);
                m.writer().writeByte(5);
            }
            for (int i = 0; i < conn.p.map.maxzone; i++) {
                m.writer().writeUTF(
                        "Khu " + (map[i].zone_id + 1) + " (" + map[i].players.size() + "/" + map[i].maxplayer + ")");
            }
            if (!Map.is_map_cant_save_site(conn.p.map.map_id)) {
                m.writer().writeUTF("Khu đi buôn");
            }
            //
            conn.addmsg(m);
            m.cleanup();
        }
    }

    private static void Menu_Alisama(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 9);
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 10);
                break;
            }
            case 2: {
                Service.send_box_UI(conn, 11);
                break;
            }
            case 3: {
                Service.send_box_UI(conn, 12);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_DaDichChuyen10(Session conn, byte index) throws IOException {
        if (conn.p.item.wear[11] != null && (conn.p.item.wear[11].id == 3599 || conn.p.item.wear[11].id == 3593
                || conn.p.item.wear[11].id == 3596)) {
            return;
        }
        Vgo vgo = null;
        switch (index) {
            case 0: {
                vgo = new Vgo();
                vgo.id_map_go = 1;
                vgo.x_new = 432;
                vgo.y_new = 354;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 1: {
                vgo = new Vgo();
                vgo.id_map_go = 33;
                vgo.x_new = 432;
                vgo.y_new = 480;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 2: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt, hãy kích hoạt");
                    return;
                }
                vgo = new Vgo();
                vgo.id_map_go = 82;
                vgo.x_new = 432;
                vgo.y_new = 354;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 3: {
                vgo = new Vgo();
                vgo.id_map_go = 4;
                vgo.x_new = 888;
                vgo.y_new = 672;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 4: {
                vgo = new Vgo();
                vgo.id_map_go = 5;
                vgo.x_new = 1056;
                vgo.y_new = 864;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 5: {
                vgo = new Vgo();
                vgo.id_map_go = 8;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 6: {
                vgo = new Vgo();
                vgo.id_map_go = 9;
                vgo.x_new = 1243;
                vgo.y_new = 876;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 7: {
                vgo = new Vgo();
                vgo.id_map_go = 11;
                vgo.x_new = 286;
                vgo.y_new = 708;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 8: {
                vgo = new Vgo();
                vgo.id_map_go = 12;
                vgo.x_new = 240;
                vgo.y_new = 732;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 9: {
                vgo = new Vgo();
                vgo.id_map_go = 13;
                vgo.x_new = 150;
                vgo.y_new = 979;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 10: {
                vgo = new Vgo();
                vgo.id_map_go = 15;
                vgo.x_new = 469;
                vgo.y_new = 1099;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 11: {
                vgo = new Vgo();
                vgo.id_map_go = 16;
                vgo.x_new = 673;
                vgo.y_new = 1093;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 12: {
                vgo = new Vgo();
                vgo.id_map_go = 17;
                vgo.x_new = 660;
                vgo.y_new = 612;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_DaDichChuyen33(Session conn, byte index) throws IOException {
        if (conn.p.item.wear[11] != null && (conn.p.item.wear[11].id == 3599 || conn.p.item.wear[11].id == 3593
                || conn.p.item.wear[11].id == 3596)) {
            return;
        }
        Vgo vgo = null;
        switch (index) {
            case 0: {
                vgo = new Vgo();
                vgo.id_map_go = 67;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 1: {
                vgo = new Vgo();
                vgo.id_map_go = 33;
                vgo.x_new = 432;
                vgo.y_new = 480;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 2: {
                // vgo = new Vgo();
                // vgo.idmapgo = 82;
                // vgo.xnew = 432;
                // vgo.ynew = 354;
                // conn.p.changemap(conn.p, vgo);
                Service.send_notice_box(conn, "Đang bảo trì khu vực này");
                break;
            }
            case 3: {
                vgo = new Vgo();
                vgo.id_map_go = 20;
                vgo.x_new = 787;
                vgo.y_new = 966;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 4: {
                vgo = new Vgo();
                vgo.id_map_go = 22;
                vgo.x_new = 120;
                vgo.y_new = 678;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 5: {
                vgo = new Vgo();
                vgo.id_map_go = 24;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 6: {
                vgo = new Vgo();
                vgo.id_map_go = 26;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 7: {
                vgo = new Vgo();
                vgo.id_map_go = 29;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 8: {
                vgo = new Vgo();
                vgo.id_map_go = 31;
                vgo.x_new = 360;
                vgo.y_new = 624;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 9: {
                vgo = new Vgo();
                vgo.id_map_go = 37;
                vgo.x_new = 150;
                vgo.y_new = 674;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 10: {
                vgo = new Vgo();
                vgo.id_map_go = 39;
                vgo.x_new = 199;
                vgo.y_new = 882;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 11: {
                vgo = new Vgo();
                vgo.id_map_go = 41;
                vgo.x_new = 187;
                vgo.y_new = 462;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 12: {
                vgo = new Vgo();
                vgo.id_map_go = 43;
                vgo.x_new = 228;
                vgo.y_new = 43;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 13: {
                vgo = new Vgo();
                vgo.id_map_go = 45;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 14: {
                vgo = new Vgo();
                vgo.id_map_go = 50;
                vgo.x_new = 300;
                vgo.y_new = 300;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_DaDichChuyen55(Session conn, byte index) throws IOException {
        if (conn.p.item.wear[11] != null && (conn.p.item.wear[11].id == 3599 || conn.p.item.wear[11].id == 3593
                || conn.p.item.wear[11].id == 3596)) {
            return;
        }
        Vgo vgo = null;
        switch (index) {
            case 0: {
                vgo = new Vgo();
                vgo.id_map_go = 67;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 1: {
                // vgo = new Vgo();
                // vgo.idmapgo = 82;
                // vgo.xnew = 432;
                // vgo.ynew = 354;
                // conn.p.changemap(conn.p, vgo);
                Service.send_notice_box(conn, "Đang bảo trì khu vực này");
                break;
            }
            case 2: {
                vgo = new Vgo();
                vgo.id_map_go = 74;
                vgo.x_new = 258;
                vgo.y_new = 354;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 3: {
                vgo = new Vgo();
                vgo.id_map_go = 77;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 4: {
                vgo = new Vgo();
                vgo.id_map_go = 93;
                vgo.x_new = 462;
                vgo.y_new = 342;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 5: {
                vgo = new Vgo();
                vgo.id_map_go = 94;
                vgo.x_new = 306;
                vgo.y_new = 240;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 6: {
                vgo = new Vgo();
                vgo.id_map_go = 95;
                vgo.x_new = 390;
                vgo.y_new = 162;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 7: {
                vgo = new Vgo();
                vgo.id_map_go = 96;
                vgo.x_new = 198;
                vgo.y_new = 666;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 8: {
                vgo = new Vgo();
                vgo.id_map_go = 97;
                vgo.x_new = 432;
                vgo.y_new = 168;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 9: {
                vgo = new Vgo();
                vgo.id_map_go = 98;
                vgo.x_new = 270;
                vgo.y_new = 132;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 10: {
                vgo = new Vgo();
                vgo.id_map_go = 33;
                vgo.x_new = 432;
                vgo.y_new = 480;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Hammer(Session conn, byte index, byte idmenu) throws IOException {
        if (idmenu == 0) {
            switch (index) {
                case 0: {
                    Service.send_box_UI(conn, 5);
                    break;
                }
                case 1: {
                    Service.send_box_UI(conn, 6);
                    break;
                }
                case 2: {
                    Service.send_box_UI(conn, 7);
                    break;
                }
                case 3: {
                    Service.send_box_UI(conn, 8);
                    break;
                }
                case 4: // chế tạo tinh tú
                {
                    send_menu_select(conn, -5, new String[]{"Chiến binh", "Sát thủ", "Pháp sư", "Xạ thủ"}, (byte) 1);
                    break;
                }
                case 5:// nâng cấp tinh tú
                {
                    conn.p.isCreateItemStar = true;
                    Service.send_box_UI(conn, 33);
                    //send_menu_select(conn,-5100,new String[]{"Chiến binh","Sát thủ","Pháp sư","Xạ thủ"});
                    break;
                }
                case 6: { // giap sieu nhan
                    if (conn.p.item.wear[20] == null) {
                        Service.send_notice_box(conn, "Mặc đéo đâu mà tháo hả thằng lol?");
                    } else {
                        Item3 buffer = conn.p.item.wear[20];
                        conn.p.item.wear[20] = null;
                        conn.p.item.add_item_bag3(buffer);
                        conn.p.item.char_inventory(3);
                        conn.p.fashion = Part_fashion.get_part(conn.p);
                        Service.send_wear(conn.p);
                        Service.send_char_main_in4(conn.p);
                        MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                        Service.send_notice_box(conn, "Tháo thành công");
                    }
                    break;
                }
                case 7: { // thao danh hiẹu
                    if (conn.p.item.wear[19] == null) {
                        Service.send_notice_box(conn, "Mặc đéo đâu mà tháo hả thằng lol?");
                    } else {
                        Item3 buffer = conn.p.item.wear[19];
                        conn.p.item.wear[19] = null;
                        conn.p.item.add_item_bag3(buffer);
                        conn.p.item.char_inventory(3);
                        conn.p.fashion = Part_fashion.get_part(conn.p);
                        Service.send_wear(conn.p);
                        Service.send_char_main_in4(conn.p);
                        MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                        Service.send_notice_box(conn, "Tháo thành công");
                    }
                    break;
                }
                case 8: {
                    send_menu_select(conn, 1000, new String[]{"Giáp Siêu Nhân Bạc(ngày)  ", "Giáp Siêu Nhan Tím(ngày) ", " Giáp Siêu Nhan Xanh(ngày)  ", "Giáp Siêu Nhân Vàng(ngày)"});
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        } else if (idmenu == 1) {
            String[] nemu = new String[]{"Nón", "Áo", "Quần", "Giày", "Găng tay", "Nhẫn", "Vũ khí", "Dây chuyền"};
            send_menu_select(conn, -5, nemu, (byte) (10 + index));
        } else if (idmenu >= 10 && idmenu <= 13) {
            conn.p.isCreateItemStar = true;
            conn.p.ClazzItemStar = (byte) (idmenu - 10);
            conn.p.TypeItemStarCreate = index;
            Service.send_box_UI(conn, 40 + index);
        }
    }

    private static void Menu_Alisama(Session conn, byte index, byte idmenu) throws IOException {
        if (idmenu == 0) {
            switch (index) {
                case 0: {
                    Service.send_box_UI(conn, 9);
                    break;
                }
                case 1: {
                    Service.send_box_UI(conn, 10);
                    break;
                }
                case 2: {
                    Service.send_box_UI(conn, 11);
                    break;
                }
                case 3: {
                    Service.send_box_UI(conn, 12);
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
            }
        }
    }

    private static void Menu_GiapSieuNhan(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.get_ngoc() < 20000 || conn.p.item.total_item_by_id(4, 319) < 200) {
                    Service.send_notice_box(conn, "Cần tối thiểu 20000 ngọc và 200 hồ quang  bạc để đổi !");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-20000);
                conn.p.item.remove(4, 319, 200);
                short iditem = 4784;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 3;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 1: {
                if (conn.p.get_ngoc() < 20000 || conn.p.item.total_item_by_id(4, 320) < 200) {
                    Service.send_notice_box(conn, "Cần tối thiểu 20000 ngọc và 200 hồ quang  tím để đổi!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-20000);
                conn.p.item.remove(4, 320, 200);
                short iditem = 4785;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 3;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 2: {
                if (conn.p.get_ngoc() < 20000 || conn.p.item.total_item_by_id(4, 321) < 200) {
                    Service.send_notice_box(conn, "Cần tối thiểu 20000 ngọc và 200 hồ quang  xanh để đổi!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-20000);
                conn.p.item.remove(4, 321, 200);
                short iditem = 4786;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 3;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 3: {
                if (conn.p.get_ngoc() < 20000 || conn.p.item.total_item_by_id(4, 322) < 200) {
                    Service.send_notice_box(conn, "Cần tối thiểu 20000 ngọc và 200 hồ quang  vàng để đổi!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Không đủ ô trống!");
                    return;
                }
                conn.p.update_ngoc(-20000);
                conn.p.item.remove(4, 322, 200);
                short iditem = 4787;
                Item3 itbag = new Item3();
                itbag.id = iditem;
                itbag.name = ItemTemplate3.item.get(iditem).getName();
                itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                itbag.type = ItemTemplate3.item.get(iditem).getType();
                itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                itbag.color = ItemTemplate3.item.get(iditem).getColor();
                itbag.part = ItemTemplate3.item.get(iditem).getPart();
                itbag.op = ItemTemplate3.item.get(iditem).getOp();
                itbag.tier = 0;
                itbag.islock = false;
                itbag.time_use = 0;
                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 3;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.char_inventory(5);

                List<box_item_template> ids = new ArrayList<>();
                ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;

            }
        }
    }

    private static void Menu_Doubar(Session conn, byte index, byte idmenu) throws IOException {
        if (idmenu == 1) {
            String s = "Có lỗi xảy ra";
            if (index == 0) {
                s = BossHDL.BossManager.GetInfoBoss(83);
            } else if (index == 1) {
                s = BossHDL.BossManager.GetInfoBoss(84);
            } else if (index == 2) {
                s = BossHDL.BossManager.GetInfoBoss(101);
            } else if (index == 3) {
                s = BossHDL.BossManager.GetInfoBoss(103);
            } else if (index == 4) {
                s = BossHDL.BossManager.GetInfoBoss(104);
            } else if (index == 5) {
                s = BossHDL.BossManager.GetInfoBoss(105);
            } else if (index == 6) {
                s = BossHDL.BossManager.GetInfoBoss(106);
            } else if (index == 7) {
                s = BossHDL.BossManager.GetInfoBoss(149);
            } else if (index == 8) {
                s = BossHDL.BossManager.GetInfoBoss(155);
            } else if (index == 9) {
                s = BossHDL.BossManager.GetInfoBoss(195);
            } else if (index == 10) {
                s = BossHDL.BossManager.GetInfoBoss(173);
            } else if (index == 11) {
                s = BossHDL.BossManager.GetInfoBoss(197);
            } else if (index == 12) {
                s = BossHDL.BossManager.GetInfoBoss(196);
            } else if (index == 13) {
                s = BossHDL.BossManager.GetInfoBoss(186);
            } else if (index == 14) {
                s = BossHDL.BossManager.GetInfoBoss(187);
            } else if (index == 15) {
                s = BossHDL.BossManager.GetInfoBoss(188);
            } else if (index == 16) {
                s = BossHDL.BossManager.GetInfoBoss(174);
            }
            Service.send_notice_box(conn, s);
            return;

        }
        // cua hang item wear
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 1);
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 2);
                break;
            }
            case 2: {
                Service.send_box_UI(conn, 3);
                break;
            }
            case 3: {
                Service.send_box_UI(conn, 4);
                break;
            }
            case 4: {
                send_menu_select(conn, -4, new String[]{
                    "Dê bạc",
                    "Dê vàng",
                    "Xà nữ",
                    "Bọ cạp chúa",
                    "Quỷ một mắt",
                    "Quỷ đầu bò",
                    "Kỵ sĩ địa ngục",
                    "Nhện chúa",
                    "Giant Skeleton",
                    "Bos Even 7x",
                    "Bos Even 8x",
                    "Bos Even 11x",
                    "Bos Even 13x",
                    "Bos Even 0x",
                    "Bos Even 1x",
                    "Bos Even 2x",
                    "Boss sự kiện"

                }, (byte) 1);
                break;
            }
            case 5: {
                Service.send_box_UI(conn, 37);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_keva(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: { // cua hang potion
                Service.send_box_UI(conn, 0);
                break;
            }
            case 1: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                send_menu_select(conn, 600, new String[]{"Khu Làng 10x", "Khu Lảng 11x", "Khu Lảng 12x", "Khu Lảng 13x", "Khu Lảng 14x"});
                break;
            }
            case 2: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                send_menu_select(conn, 601, new String[]{"Khu Boss Even 0x", "Khu Boss Even 1x", "Khu Boss Even 2x", "Khu Boss Even 7x", "Khu Boss Even 8x", "Khu Boss Even 11x", "Khu Boss Even 13x"});
                break;
            }
            case 3: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 103;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 4: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 1;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Mr_Haku(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_vang() < 500) {
                    Service.send_notice_box(conn, "Không đủ 500 vàng");
                    return;
                }
                conn.p.update_vang(-500);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 67;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);

                break;
            }

            default:
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
        }
    }

    private static void Menu_Langphusuongup(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 104;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 1: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 105;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 2: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 106;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 3: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 107;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 4: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 108;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Langphusuongboss(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 109;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 1: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 110;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 2: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 111;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 3: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 112;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 4: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 113;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 5: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 114;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 6: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 115;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Lisa(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: { // cua hang potion
                Service.send_box_UI(conn, 0);
                break;
            }
            case 1: {
                if (conn.p.item.total_item_by_id(4, 52) > 0) {
                    MoLy.show_table_to_choose_item(conn.p);
                } else {
                    Service.send_notice_box(conn, "Không đủ vé mở trong hành trang");
                }
                break;
            }
            case 2: { // cua hang potion
                Service.send_box_input_text(conn, 22, "% thuế", new String[]{"Nhập % thuế 0 - 5%"});
                break;
            }
            case 3: {
                Member_ChienTruong temp = ChienTruong.gI().get_bxh(conn.p.name);
                if (temp != null) {
                    switch (ChienTruong.gI().get_index_bxh(temp)) {
                        case 0: {
                            short[] id_ = new short[]{3, 2, 53, 54, 18};
                            short[] id2_ = new short[]{5, 5, 1, 1, 10};
                            short[] id3_ = new short[]{7, 7, 4, 4, 4};
                            for (int i = 0; i < id_.length; i++) {
                                Item47 it = new Item47();
                                it.id = id_[i];
                                it.quantity = id2_[i];
                                conn.p.item.add_item_bag47(id3_[i], it);
                            }
                            break;
                        }
                        case 1:
                        case 2: {
                            short[] id_ = new short[]{3, 2, 18};
                            short[] id2_ = new short[]{5, 5, 10};
                            short[] id3_ = new short[]{7, 7, 4};
                            for (int i = 0; i < id_.length; i++) {
                                Item47 it = new Item47();
                                it.id = id_[i];
                                it.quantity = id2_[i];
                                conn.p.item.add_item_bag47(id3_[i], it);
                            }
                            break;
                        }
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9: {
                            short[] id_ = new short[]{3, 18};
                            short[] id2_ = new short[]{5, 10};
                            short[] id3_ = new short[]{7, 4};
                            for (int i = 0; i < id_.length; i++) {
                                Item47 it = new Item47();
                                it.id = id_[i];
                                it.quantity = id2_[i];
                                conn.p.item.add_item_bag47(id3_[i], it);
                            }
                            break;
                        }
                    }
                } else {
                    Service.send_notice_box(conn, "Không có tên trong danh sách");
                }
                break;
            }
            case 5: { // cua hang potion
                ChiemThanhManager.NhanQua(conn.p);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Emma(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: { // cua hang potion
                Service.send_box_UI(conn, 0);
                break;
            }
            case 1: {
                if (conn.p.item.total_item_by_id(4, 52) > 0) {
                    MoLy.show_table_to_choose_item(conn.p);
                } else {
                    Service.send_notice_box(conn, "Không đủ vé mở trong hành trang");
                }
                break;
            }
            case 2: { // cua hang potion
                Service.send_box_input_text(conn, 22, "% thuế", new String[]{"Nhập % thuế 5 - 20%"});
                break;
            }
            case 3: {
                Member_ChienTruong temp = ChienTruong.gI().get_bxh(conn.p.name);
                if (temp != null) {
                    switch (ChienTruong.gI().get_index_bxh(temp)) {
                        case 0: {
                            short[] id_ = new short[]{3, 2, 53, 54, 18};
                            short[] id2_ = new short[]{5, 5, 1, 1, 10};
                            short[] id3_ = new short[]{7, 7, 4, 4, 4};
                            for (int i = 0; i < id_.length; i++) {
                                Item47 it = new Item47();
                                it.id = id_[i];
                                it.quantity = id2_[i];
                                conn.p.item.add_item_bag47(id3_[i], it);
                            }
                            break;
                        }
                        case 1:
                        case 2: {
                            short[] id_ = new short[]{3, 2, 18};
                            short[] id2_ = new short[]{5, 5, 10};
                            short[] id3_ = new short[]{7, 7, 4};
                            for (int i = 0; i < id_.length; i++) {
                                Item47 it = new Item47();
                                it.id = id_[i];
                                it.quantity = id2_[i];
                                conn.p.item.add_item_bag47(id3_[i], it);
                            }
                            break;
                        }
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9: {
                            short[] id_ = new short[]{3, 18};
                            short[] id2_ = new short[]{5, 10};
                            short[] id3_ = new short[]{7, 4};
                            for (int i = 0; i < id_.length; i++) {
                                Item47 it = new Item47();
                                it.id = id_[i];
                                it.quantity = id2_[i];
                                conn.p.item.add_item_bag47(id3_[i], it);
                            }
                            break;
                        }
                    }
                } else {
                    Service.send_notice_box(conn, "Không có tên trong danh sách");
                }
                break;
            }
            case 5: { // cua hang potion
                ChiemThanhManager.NhanQua(conn.p);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_CayThong(Session conn, byte index) throws IOException {
        if (Manager.gI().event == 1) {
            switch (index) {
                case 0:
                case 1:
                case 2:
                case 3: {
                    int quant = conn.p.item.total_item_by_id(4, (113 + index));
                    if (quant > 0) {
                        //
                        short[] id_4 = new short[]{2, 5, 52, 142, 225, 271};
                        short[] id_7 = new short[]{0, 4, 23, 34, 39, 352, 357, 362, 367, 372, 377, 382, 387, 392, 397, 402,
                            407, 412,};
                        HashMap<Short, Short> list_4 = new HashMap<>();
                        HashMap<Short, Short> list_7 = new HashMap<>();
                        for (int i = 0; i < quant; i++) {
                            if (conn.p.item.get_bag_able() > 1) {
                                if (80 > Util.random(100)) {
                                    Item47 it = new Item47();
                                    it.category = 4;
                                    it.id = id_4[Util.random(id_4.length)];
                                    it.quantity = (short) Util.random(1, 3);
                                    if (!list_4.containsKey(it.id)) {
                                        list_4.put(it.id, it.quantity);
                                    } else {
                                        short quant_ = it.quantity;
                                        list_4.put(it.id, (short) (list_4.get(it.id) + quant_));
                                    }
                                    conn.p.item.add_item_bag47(4, it);
                                } else {
                                    Item47 it = new Item47();
                                    it.category = 7;
                                    it.id = id_7[Util.random(id_7.length)];
                                    it.quantity = (short) Util.random(1, 2);
                                    if (!list_7.containsKey(it.id)) {
                                        list_7.put(it.id, it.quantity);
                                    } else {
                                        short quant_ = it.quantity;
                                        list_7.put(it.id, (short) (list_7.get(it.id) + quant_));
                                    }
                                    conn.p.item.add_item_bag47(7, it);
                                }
                            }
                        }
                        //
                        Event_1.add_caythong(conn.p.name, quant);
                        conn.p.item.remove(4, (113 + index), quant);
                        conn.p.item.char_inventory(4);
                        conn.p.item.char_inventory(7);
                        String item_receiv = "\n";
                        for (Entry<Short, Short> en : list_4.entrySet()) {
                            item_receiv += ItemTemplate4.item.get(en.getKey()).getName() + " " + en.getValue() + "\n";
                        }
                        for (Entry<Short, Short> en : list_7.entrySet()) {
                            item_receiv += ItemTemplate7.item.get(en.getKey()).getName() + " " + en.getValue() + "\n";
                        }
                        Service.send_notice_box(conn, "Trang trí thành công " + quant + " lần và nhận được:" + item_receiv);
                    } else {
                        Service.send_notice_box(conn, "Không đủ trong hành trang!");
                    }
                    break;
                }
                case 4: {
                    send_menu_select(conn, 120, Event_1.get_top_caythong());
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Đang bảo trì");
                    break;
                }
            }
        }
    }

    private static void Menu_ThaoKhamNgoc(Session conn, byte index) throws IOException {
        if (conn.p.list_thao_kham_ngoc.size() > 0) {
            if (conn.p.item.get_bag_able() < 3) {
                Service.send_notice_box(conn, "Hành trang không đủ chỗ");
                return;
            }
            Item3 it = conn.p.list_thao_kham_ngoc.get(index);
            if (it != null) {
                for (int i = it.op.size() - 1; i >= 0; i--) {
                    byte id = it.op.get(i).id;
                    if (id == 58 || id == 59 || id == 60) {
                        if (it.op.get(i).getParam(0) != -1) {
                            Item47 it_add = new Item47();
                            it_add.id = (short) (it.op.get(i).getParam(0));
                            it_add.quantity = 1;
                            it_add.category = 7;
                            conn.p.item.add_item_bag47(7, it_add);
                        }
                        it.op.get(i).setParam(-1);
                    } else if (id >= 100 && id <= 107) {
                        it.op.remove(i);
                    }
                }
//                for (int i = 0; i < it.op.size(); i++) {
//                    if (it.op.get(i).id == 58) {
//                        if (it.op.get(i).getParam(0) != -1) {
//                            Item47 it_add = new Item47();
//                            it_add.id = (short) (it.op.get(i).getParam(0));
//                            it_add.quantity = 1;
//                            it_add.category = 7;
//                            conn.p.item.add_item_bag47(7, it_add);
//                        }
//                        it.op.get(i).setParam(-1);
//                    }
//                    if (it.op.get(i).id == 59) {
//                        if (it.op.get(i).getParam(0) != -1) {
//                            Item47 it_add = new Item47();
//                            it_add.id = (short) (it.op.get(i).getParam(0));
//                            it_add.quantity = 1;
//                            it_add.category = 7;
//                            conn.p.item.add_item_bag47(7, it_add);
//                        }
//                        it.op.get(i).setParam(-1);
//                    }
//                    if (it.op.get(i).id == 60) {
//                        if (it.op.get(i).getParam(0) != -1) {
//                            Item47 it_add = new Item47();
//                            it_add.id = (short) (it.op.get(i).getParam(0));
//                            it_add.quantity = 1;
//                            it_add.category = 7;
//                            conn.p.item.add_item_bag47(7, it_add);
//                        }
//                        it.op.get(i).setParam(-1);
//                    }
//                }
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(3);
                Service.send_wear(conn.p);
                Service.send_notice_box(conn, "Tháo thành công");
            }
        }
    }

    private static void Menu_DoiDongMeDaySTG(Session conn, byte index) throws IOException {
        if (conn.p.item.wear != null && conn.p.item.wear.length > 12 && Helps.CheckItem.isMeDay(conn.p.item.wear[12].id)) {
            Service.send_box_input_yesno(conn, 94, "Thực hiện này sẽ tiêu tốn 1000 ngọc, bạn có chắc chắn?");
        } else {
            Service.send_notice_box(conn, "Không có vật phẩm phù hợp!");
        }
    }

    private static void Menu_DoiDongMeDaySTPT(Session conn, byte index) throws IOException {
        if (conn.p.item.wear != null && conn.p.item.wear.length > 12 && Helps.CheckItem.isMeDay(conn.p.item.wear[12].id)) {
            Service.send_box_input_yesno(conn, 98, "Thực hiện này sẽ tiêu tốn 1000 ngọc, bạn có chắc chắn?");
        } else {
            Service.send_notice_box(conn, "Không có vật phẩm phù hợp!");
        }
    }

    private static void Menu_Quest_Daily(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                String notice
                        = "Nhiệm vụ Ngày: đánh quái ngẫu nhiên theo level, tối đa ngày nhận 20 nhiệm vụ, mỗi nhiệm vụ sẽ nhận được phần thưởng kinh nghiệm, ngọc và có cơ hội nhận nguyên liệu mề đay."
                        + "\n Dễ : Vàng Ngọc + Exp" + "\n Bình Thường : Vàng Ngọc, Exp + NL mề Xanh"
                        + "\n Khó :Vàng Ngọc, Exp + NL mề Vàng" + "\n Siêu Khó : Vàng Ngọc, Exp + NL mề Tím";
                Service.send_notice_box(conn, notice);
                break;
            }
            case 1: {
                if (conn.p.quest_daily[0] != -1) {
                    Service.send_notice_box(conn, "Đã nhận nhiệm vụ rồi!");
                } else {
                    if (conn.p.quest_daily[4] > 0) {
                        send_menu_select(conn, 999, new String[]{"Cực Dễ", "Bình thường", "Khó", "Siêu Khó"});
                    } else {
                        Service.send_notice_box(conn, "Hôm nay đã hết lượt, quay lại vào ngày mai");
                    }
                }
                break;
            }
            case 2: {
                DailyQuest.remove_quest(conn.p);
                break;
            }
            case 3: {
                DailyQuest.finish_quest(conn.p);
                break;
            }
            case 4: {
                Service.send_notice_box(conn, DailyQuest.info_quest(conn.p));
                break;
            }
        }
    }

    // private static void Menu_Quest(Session conn, byte index) throws IOException {
    // switch (index) {
    //
    // case 0: {
    // send_menu_select(conn, 1000, new String[] {"Hướng dẫn", "Nhận nhiệm vụ", "Hủy nhiệm vụ", "Trả
    // nhiệm vụ", "Kiểm tra"});
    // break;
    // }
    // }
    // }
    private static void Menu_Wedding(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.item.wear[23] == null) {
                    Service.send_box_input_text(conn, 66, "Nhập thông tin",
                            new String[]{"Chọn nhẫn (1-4) : ", "Tên đối phương : "});
                } else {
                    Service.send_notice_box(conn, "Nhẫn cưới thì đeo đấy mà đòi cưới thêm ai??");
                }
                break;
            }
            case 1: {
                if (conn.p.item.wear[23] != null) {
                    Wedding temp = Wedding.get_obj(conn.p.name);
                    if (temp != null) {
                        String name_target = "";
                        if (temp.name_1.equals(conn.p.name)) {
                            name_target = temp.name_2;
                        } else {
                            name_target = temp.name_1;
                        }
                        Service.send_box_input_yesno(conn, 111, "Xác định hủy hôn ước với " + name_target);
                    }
                } else {
                    Service.send_notice_box(conn, "Đã cưới ai đâu, ảo tưởng à??");
                }
                break;
            }
            case 2: {
                Item3 it = conn.p.item.wear[23];
                if (it != null) {
                    float perc = (((float) Wedding.get_obj(conn.p.name).exp) / Level.entrys.get(it.tier).exp) * 100f;
                    String notice = "Exp hiện tại : %s, nâng cấp cần %str vàng và %sk ngọc";
                    String a = String.format("%.2f", perc) + "%";
                    Service.send_box_input_yesno(conn, 112,
                            String.format(notice, a, (3 * (it.tier + 1)), (3 * (it.tier + 1))));
                } else {
                    Service.send_notice_box(conn, "Đã cưới ai éo đâu, ảo tưởng à??");
                }
                break;
            }
            case 3: {
                String notice = "Nhẫn cưới\r\n" + "- 3 tỷ vàng \" nhẫn cưới 1\" \r\n" + "- 300k ngọc \" nhẫn cưới 2\"\r\n"
                        + "- 600k ngọc \" nhẫn cưới 3\"\r\n" + "- 900k ngọc \" nhẫn cưới 4\"\r\n" + "Nâng cấp nhẫn:\r\n"
                        + "Khi đã kết hôn vợ và chồng cùng chung 1 nhóm đi up quái hoặc giết boss thì nhẫn cưới sẽ đc + exp\r\n"
                        + "Đến npc Anna để tiến hành nâng cấp nhẫn, mỗi lần nâng cấp tốn 3k ngọc 3tr vàng.\r\n"
                        + "Cấp tối đa của nhẫn là 30. \r\n" + "Lưu ý: Mỗi lần nâng cấp số vàng và ngọc sẽ nhân lên \r\n"
                        + "Ví dụ: Lever 1-2 phí 3k và 3tr lên level 2-3 phí sẽ nhân lên 6k và 6tr vàng";
                Service.send_notice_box(conn, notice);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }
}
