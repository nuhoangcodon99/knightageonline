package client;

import core.GameSrc;
import event.Event_1;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import core.Log;
import core.Manager;
import core.SQL;
import core.Service;
import core.Util;

import io.Message;
import io.Session;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import map.Map;
import map.MapService;
import map.Vgo;
import template.Clan_mems;
import template.Item3;
import template.Item47;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.Level;
import template.box_item_template;

public class TextFromClient {

    public static void process(Session conn, Message m2) throws IOException {
        short idnpc = m2.reader().readShort();
        short idmenu = m2.reader().readShort();
        byte size = m2.reader().readByte();
        if (idmenu != 0) {
            return;
        }
        switch (idnpc) {
             
             case 30: {
                if (size != 3) {
                    return;
                }
                String value1 = m2.reader().readUTF();
                String value2 = m2.reader().readUTF();
                String value3 = m2.reader().readUTF();

                if (!value1.equals(conn.pass)) {
                    Service.send_notice_box(conn, "Mật khẩu không đúng");
                    return;
                }
                if (value2.equals(value1) || !value2.equals(value3)) {
                    Service.send_notice_box(conn, "Mật khẩu mới không hợp lệ");
                    return;
                }
                try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement()) {
                    st.execute("UPDATE `account` SET `pass` = '" + value2 + "' WHERE `user` = '" + conn.user + "';");
                    connection.commit();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Service.send_notice_box(conn, "Có lỗi xảy ra");
                    return;
                }
                Service.send_notice_box(conn, " Đổi mật khẩu mới thành công");

                break;
            }
                case 66: {
                if (size != 2) {
                    return;
                }
                String value1 = m2.reader().readUTF();
                String value2 = m2.reader().readUTF();
                if (!(Util.isnumber(value1))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int quant = Integer.parseInt(value1);
                Player p0 = Map.get_player_by_name(value2);
                if (p0 != null) {
                    if (p0.item.wear[23] != null) {
                        Service.send_notice_box(conn, "Đối phương đã kết đôi với người khác!");
                        return;
                    }
                    if (p0.level < 60) {
                        Service.send_notice_box(conn, "Yêu cầu level trên 60");
                        return;
                    }
                    switch (quant) {
                        case 1: {
                            if (conn.p.get_vang() < 3_000_000_000L) {
                                Service.send_notice_box(conn, "Không đủ 3 tỷ vàng");
                                return;
                            }
                            conn.p.update_vang(-3_000_000_000L);
                            break;
                        }
                        case 2: {
                            if (conn.p.get_ngoc() < 300_000) {
                                Service.send_notice_box(conn, "Không đủ 300k ngọc");
                                return;
                            }
                            conn.p.update_ngoc(-300_000);
                            break;
                        }
                        case 3: {
                            if (conn.p.get_ngoc() < 600_000) {
                                Service.send_notice_box(conn, "Không đủ 600k ngọc");
                                return;
                            }
                            conn.p.update_ngoc(-600_000);
                            break;
                        }
                        case 4: {
                            if (conn.p.get_ngoc() < 900_000) {
                                Service.send_notice_box(conn, "Không đủ 900k ngọc");
                                return;
                            }
                            conn.p.update_ngoc(-900_000);
                            break;
                        }
                        default: {
                            Service.send_notice_box(conn, "Chọn nhẫn từ 1 - 4 thôi!");
                            return;
                        }
                    }
                    conn.p.item.char_inventory(5);
                    p0.in4_wedding = new String[]{"" + quant, conn.p.name};
                    Service.send_box_input_yesno(p0.conn, 110, conn.p.name + " muốn cầu hôn bạn, đồng ý lấy mình nhé?");
                } else {
                    Service.send_notice_box(conn, "Không tìm thấy đối phương!");
                }
                break;
            }
            case 16: {
                if (size != 1) {
                    return;
                }
                String value = m2.reader().readUTF();
                if (!(Util.isnumber(value))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int quant = Integer.parseInt(value);
                if (quant > 32_000 || quant <= 0) {
                    Service.send_notice_box(conn, "Số lượng không hợp lệ!");
                    return;
                }
                int quant_ngoc_can_create = conn.p.item.total_item_by_id(7, conn.p.id_ngoc_tinh_luyen);
                if (quant > quant_ngoc_can_create) {
                    Service.send_notice_box(conn, "Số lượng trong hành trang không đủ!");
                    return;
                }
                int vang_required
                        = (int) (((long) quant) * (GameSrc.get_vang_hopngoc(conn.p.id_ngoc_tinh_luyen) / 50_000L) * 1_000_000L);
                if (conn.p.get_vang() < vang_required) {
                    Service.send_notice_box(conn, "Không đủ " + vang_required + " vàng");
                    return;
                }
                if (conn.p.get_vang() < vang_required) {
                    Service.send_notice_box(conn, "Tinh luyện cần " + vang_required + " vàng!");
                    return;
                }
                conn.p.update_vang(-vang_required);
                Item47 it = new Item47();
                it.id = (short) (conn.p.id_ngoc_tinh_luyen + 30);
                it.quantity = (short) quant;
                conn.p.item.add_item_bag47(7, it);
                conn.p.item.remove(7, conn.p.id_ngoc_tinh_luyen, quant);
                Service.send_notice_box(conn,
                        "Tinh luyện thành công " + quant + " " + ItemTemplate7.item.get(it.id).getName());
                conn.p.id_ngoc_tinh_luyen = -1;
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(3);
                break;
            }
            case 0: {
                if (size != 1) {
                    return;
                }
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt, hãy kích hoạt");
                    return;
                }
                String text = m2.reader().readUTF();
                text = text.toLowerCase();
                Pattern p = Pattern.compile("^[a-zA-Z0-9]{1,15}$");
                if (!p.matcher(text).matches()) {
                    Service.send_notice_box(conn, "Đã xảy ra lỗi");
                    return;
                }
                for (String txt : conn.p.giftcode) {
                    txt = txt.toLowerCase();
                    if (txt.equals((text)) && conn.ac_admin < 4) {
                        Service.send_notice_box(conn, "Bạn đã nhập giftcode này rồi");
                        return;
                    }
                }
                try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `giftcode` WHERE `giftname` = '" + text + "';")) {
                    byte empty_box = (byte) 0;
                    if (!rs.next()) {
                        Service.send_notice_box(conn, "Giftcode đã được nhập hoặc không tồn tại");
                    } else {
                        List<Short> IDs = new ArrayList<>();
                        List<Integer> Quants = new ArrayList<>();
                        List<Short> Types = new ArrayList<>();
                        empty_box = rs.getByte("empty_box");
                        int limit = rs.getInt("limit");
                        if (limit < 1 && conn.ac_admin < 4) {
                            Service.send_notice_box(conn, "Đã hết lượt dùng giftcode này");
                        } else if (conn.p.item.get_bag_able() >= empty_box) {
                            conn.p.giftcode.add(text);
                            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
                            for (int i = 0; i < jsar.size(); i++) {
                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                                Item3 itbag = new Item3();
                                short it = Short.parseShort(jsar2.get(0).toString());
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
                                IDs.add(it);
                                Quants.add((int) 1);
                                Types.add((short) 3);
                                conn.p.item.add_item_bag3(itbag);
                            }
                            jsar.clear();
                            //
                            jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
                            for (int i = 0; i < jsar.size(); i++) {
                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                                Item47 itbag = new Item47();
                                itbag.id = Short.parseShort(jsar2.get(0).toString());
                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                                itbag.category = 4;
                                IDs.add(itbag.id);
                                Quants.add((int) itbag.quantity);
                                Types.add((short) itbag.category);
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
                                IDs.add(itbag.id);
                                Quants.add((int) itbag.quantity);
                                Types.add((short) itbag.category);
                                conn.p.item.add_item_bag47(7, itbag);

                            }
                            jsar.clear();
                            //
                            String table = rs.getString("item47random");

                            if (table != null) {
                                jsar = (JSONArray) JSONValue.parse(table);
                                List<Item47> item47Ran = new ArrayList<>();
                                for (int i = 0; jsar != null && i < jsar.size(); i++) {
                                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                                    Item47 itbag = new Item47();
                                    itbag.id = Short.parseShort(jsar2.get(0).toString());
                                    itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                                    itbag.category = Byte.parseByte(jsar2.get(2).toString());
                                    item47Ran.add(itbag);
                                    //conn.p.item.add_item_bag47(7, itbag);
                                }
                                if (item47Ran != null && !item47Ran.isEmpty());
                                {
                                    byte count_item_random = rs.getByte("countrandom");

                                    for (int i = 0; i < count_item_random; i++) {
                                        Item47 itbag = item47Ran.get(Util.random(0, item47Ran.size()));
                                        if (itbag != null) {
                                            IDs.add(itbag.id);
                                            Quants.add((int) itbag.quantity);
                                            Types.add((short) itbag.category);
                                            conn.p.item.add_item_bag47(itbag.category, itbag);
                                        }

                                    }

                                }
                                if (jsar != null) {
                                    jsar.clear();
                                }
                            }

                            //
//                            int vang_up = rs.getInt("vang");
                            long vang_up = rs.getLong("vang");
                            int ngoc_up = rs.getInt("ngoc");
                            conn.p.update_vang(vang_up);
                            conn.p.update_ngoc(ngoc_up);
                            if (vang_up != 0) {
                                IDs.add((short) -1);
                                Quants.add((int) (vang_up > 2_000_000_000 ? 2_000_000_000 : vang_up));
                                Types.add((short) 4);
                            }
                            if (ngoc_up != 0) {
                                IDs.add((short) -2);
                                Quants.add((int) (ngoc_up > 2_000_000_000 ? 2_000_000_000 : ngoc_up));
                                Types.add((short) 4);
                            }
                            Log.gI().add_log(conn.p.name,
                                    "Nhận giftcode " + text + " : " + Util.number_format(vang_up) + " vàng");
                            Log.gI().add_log(conn.p.name,
                                    "Nhận giftcode " + text + " : " + Util.number_format(ngoc_up) + " ngọc");
//                            conn.p.item.char_inventory(3);
//                            conn.p.item.char_inventory(4);
//                            conn.p.item.char_inventory(7);
                            short[] ar_id = new short[IDs.size()];
                            int[] ar_quant = new int[Quants.size()];
                            short[] ar_type = new short[Types.size()];
                            for (int i = 0; i < ar_id.length; i++) {
                                ar_id[i] = IDs.get(i);
                                ar_quant[i] = Quants.get(i);
                                ar_type[i] = Types.get(i);
                            }
                            Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ar_id, ar_quant, ar_type);
                            //Service.send_notice_box(conn, "Nhận thành công giftcode");
                        } else {
                            Service.send_notice_box(conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 1: {
                if (conn.ac_admin > 3) {
                    if (size != 3) {
                        return;
                    }
                    String type = m2.reader().readUTF();
                    String id = m2.reader().readUTF();
                    String quantity = m2.reader().readUTF();
                    if (!(Util.isnumber(id) && Util.isnumber(quantity))) {
                        Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                        return;
                    }
                    Short sl = Short.parseShort(quantity);
                    if (sl > 32_000 || sl <= 0) {
                        Service.send_notice_box(conn, "Số lượng không hợp lệ!");
                        return;
                    }
                    if (conn.p.item.get_bag_able() > 0) {
                        switch (type) {
                            case "3": {
                                short iditem = (short) Integer.parseInt(id);
                                if (iditem > (ItemTemplate3.item.size() - 1) || iditem < 0) {
                                    return;
                                }
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
                                conn.p.item.char_inventory(3);
                                break;
                            }
                            case "4": {
                                short iditem = (short) Integer.parseInt(id);
                                if (iditem > (ItemTemplate4.item.size() - 1) || iditem < 0) {
                                    return;
                                }
                                Item47 itbag = new Item47();
                                itbag.id = iditem;
                                itbag.quantity = sl;
                                itbag.category = 4;
                                conn.p.item.add_item_bag47(4, itbag);
                                conn.p.item.char_inventory(4);
                                break;
                            }
                            case "7": {
                                short iditem = (short) Integer.parseInt(id);
                                if (iditem > (ItemTemplate7.item.size() - 1) || iditem < 0) {
                                    return;
                                }
                                Item47 itbag = new Item47();
                                itbag.id = iditem;
                                itbag.quantity = Short.parseShort(quantity);
                                itbag.category = 7;
                                conn.p.item.add_item_bag47(7, itbag);
                                conn.p.item.char_inventory(7);
                                break;
                            }
                        }
                        Service.send_notice_box(conn, "Nhận Item thành công");
                    }
                }
                break;
            }
            case 2: {
                if (conn.ac_admin > 3) {
                    if (size != 1) {
                        return;
                    }
                    String level = m2.reader().readUTF();
                    if (!(Util.isnumber(level))) {
                        Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                        return;
                    }
                    int levelchange = Integer.parseInt(level);
                    if (levelchange > 32000 || levelchange <= 0) {
                        Service.send_notice_box(conn, "Số lượng không hợp lệ!");
                        return;
                    }
                    if (levelchange < 2) {
                        levelchange = 2;
                    }
                    if (levelchange > Manager.gI().lvmax) {
                        levelchange = Manager.gI().lvmax;
                    }
                    conn.p.level = (short) (levelchange - 1);
                    conn.p.exp = Level.entrys.get(levelchange - 2).exp - 1;
                    conn.p.tiemnang = (short) (1 + Level.get_tiemnang_by_level(conn.p.level - 1));
                    conn.p.kynang = (short) (1 + Level.get_kynang_by_level(conn.p.level - 1));
                    conn.p.point1 = (short) (4 + conn.p.level);
                    conn.p.point2 = (short) (4 + conn.p.level);
                    conn.p.point3 = (short) (4 + conn.p.level);
                    conn.p.point4 = (short) (4 + conn.p.level);
                    conn.p.update_Exp(1, false);
                    Service.send_char_main_in4(conn.p);
                    for (int i = 0; i < conn.p.map.players.size(); i++) {
                        Player p0 = conn.p.map.players.get(i);
                        if (p0.index != conn.p.index && (Math.abs(p0.x - conn.p.x) < 200) && (Math.abs(p0.y - conn.p.y) < 200)) {
                            MapService.send_in4_other_char(p0.map, p0, conn.p);
                        }
                    }
                    Service.send_notice_box(conn, "Up level thành công");
                }
                break;
            }
            case 3: {

                if (size != 1) {
                    return;
                }
                String vang_join = m2.reader().readUTF();
                if (!(Util.isnumber(vang_join))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int vang_join_vxmm = Integer.parseInt(vang_join);
                if (vang_join_vxmm <= 0 || vang_join_vxmm > 2000000000) {
                    Service.send_notice_box(conn, "Số lượng nhập vào không hợp lệ");
                    return;
                }
                if (vang_join_vxmm < 10_000_000 || conn.p.get_vang() < vang_join_vxmm) {
                    Service.send_notice_box(conn, "vàng không đủ!");
                    return;
                }
                if (vang_join_vxmm > 200_000_000) {
                    Service.send_notice_box(conn, "tối đa 200tr vàng!");
                    return;
                }
                Manager.gI().vxmm.join_vxmm(conn.p, vang_join_vxmm);
                break;
            }
            case 4: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                if (size != 1) {
                    return;
                }
                String xp = m2.reader().readUTF();
                if (!(Util.isnumber(xp))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int xp_ = Integer.parseInt(xp);
                if (xp_ <= 0 || xp_ > 2000000000) {
                    Service.send_notice_box(conn, "Số lượng nhập vào không hợp lệ!");
                    return;
                }
                if (xp_ > 1) {
                    Manager.gI().exp = xp_;
                }
                Service.send_notice_box(conn, "Thay đổi xp thành công x" + Util.number_format(xp_));
                break;
            }
            case 5: {
                if (size != 1) {
                    return;
                }
                String value = m2.reader().readUTF();
                if (!(Util.isnumber(value))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int coin_exchange = Integer.parseInt(value);
                if (coin_exchange < 1000 || coin_exchange > 300_000) {
                    Service.send_notice_box(conn, "Chỉ có thể đổi tối thiểu là 1k và tối đa là 300k");
                    return;
                }
                if (conn.p.update_coin(-coin_exchange)) {
                    conn.p.update_ngoc((int) (coin_exchange / 2));
                    conn.p.item.char_inventory(5);
                    Service.send_notice_box(conn, "Đổi thành công");
//                    Log.gI().add_log(conn.p.name,
//                                    "đổi coin sang ngọc " + text + " : " + Util.number_format(ngoc_up) + " ngọc");
                } else {
                    Service.send_notice_box(conn, "Thất bại xin hãy thử lại");
                }
                break;
            }
            case 6: {
                if (size != 2) {
                    return;
                }
                String value1 = m2.reader().readUTF();
                String value2 = m2.reader().readUTF();
                Pattern p = Pattern.compile("^[a-zA-Z0-9]{5,15}$");
                if (!p.matcher(value1).matches() || !p.matcher(value2).matches()) {
                    Service.send_notice_box(conn, "Ký tự không hợp lệ, hãy thử lại");
                    return;
                }
                //
                // try (Connection connnect = SQL.gI().getConnection();
                // PreparedStatement ps = connnect.prepareStatement(
                // "INSERT INTO `sm_hso2`.`account` (`user`, `pass`, `char`, `status`, `lock`, `coin`) VALUES ('"
                // + value1 + "', '" + value2 + "', '[]', 0, 0, 0)")) {
                // if (!ps.execute()) {
                // connnect.commit();
                // }
                // } catch (SQLException e) {
                // e.printStackTrace();
                // return;
                // }
                String query = "UPDATE `account` SET `user` = '" + value1 + "', `pass` = '" + value2 + "' WHERE `user` = '"
                        + conn.user + "' LIMIT 1";
                try (Connection connnect = SQL.gI().getConnection(); Statement statement = connnect.createStatement();) {
                    if (statement.executeUpdate(query) > 0) {
                        connnect.commit();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Service.send_notice_box(conn, "Có lỗi xảy ra hoặc tên đã được sử dụng, hãy thử lại");
                    return;
                }
                Message md = new Message(31);
                md.writer().writeUTF(value1);
                md.writer().writeUTF(value2);
                conn.addmsg(md);
                md.cleanup();
                conn.user = value1;
                conn.pass = value2;
                Service.send_notice_box(conn,
                        "Đăng ký thành công tài khoản :\n Tên đăng nhập : " + value1 + "\nMật khẩu : " + value2);
                break;
            }
            case 7: {
                if (size != 1 || conn.p.fusion_material_medal_id == -1) {
                    return;
                }
                String value = m2.reader().readUTF();
                if (!(Util.isnumber(value))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int quant = Integer.parseInt(value);
                if (quant > 32000 || quant <= 0) {
                    Service.send_notice_box(conn, "Số lượng không hợp lệ");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Hành trang đầy!");
                    return;
                }
                int quant_inbag = conn.p.item.total_item_by_id(7, conn.p.fusion_material_medal_id);
                int quant_real = quant_inbag / 5;
                short id_next_material = (short) (conn.p.fusion_material_medal_id + 100);
                String name_next_material = ItemTemplate7.item.get(id_next_material).getName();
                if ((quant_real - quant) >= 0) {
                    if ((quant * 5000) > conn.p.get_vang()) {
                        Service.send_notice_box(conn, "Vàng không đủ!");
                        return;
                    }

                    conn.p.update_vang(-(quant * 5000));
                    conn.p.item.remove(7, conn.p.fusion_material_medal_id, (quant * 5));
                    Item47 it = new Item47();
                    it.id = id_next_material;
                    it.quantity = (short) quant;
                    conn.p.item.add_item_bag47(7, it);
                    conn.p.item.char_inventory(7);
                    //
                    Message m = new Message(-105);
                    m.writer().writeByte(2);
                    m.writer().writeByte(3);
                    m.writer().writeUTF("Chúc mừng bạn nhận được " + quant + " " + name_next_material);
                    m.writer().writeShort(id_next_material);
                    m.writer().writeByte(7);
                    conn.addmsg(m);
                    m.cleanup();
                } else {
                    Service.send_notice_box(conn, "Chỉ có thể hợp thành tối đa " + quant_real + " " + name_next_material);
                }
                conn.p.fusion_material_medal_id = -1;
                break;
            }
            case 8: {
//                if (size != 1) {
//                    return;
//                }
//                String value = m2.reader().readUTF();
//                if (!(Util.isnumber(value))) {
//                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
//                    return;
//                }
//                int coin_exchange = Integer.parseInt(value);
//                if (coin_exchange <= 0 || coin_exchange > 1_000_000_000) {
//                    Service.send_notice_box(conn, "Số nhập không hợp lệ, hãy thử lại");
//                    return;
//                }
//                if (conn.p.update_coin(-coin_exchange)) {
//                    conn.p.update_vang(coin_exchange * 5000);
//                    conn.p.item.char_inventory(5);
//                    Service.send_notice_box(conn, "Đổi thành công");
//                }
//                break;
            }
            case 9: {
                if (size != 1) {
                    return;
                }
                String value = m2.reader().readUTF();
                if (!(Util.isnumber(value))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int quant = Integer.parseInt(value);
                if (quant > 2_000_000_000 || quant <= 0) {
                    Service.send_notice_box(conn, "Số lượng không hợp lệ!");
                    return;
                }
                if (idnpc == 8) {
                    if (quant > conn.p.get_vang()) {
                        Service.send_notice_box(conn, "Vàng không đủ!");
                        return;
                    }
                    conn.p.myclan.member_contribute_vang(conn, quant);
                } else {
                    if (quant > conn.p.get_ngoc()) {
                        Service.send_notice_box(conn, "Ngọc không đủ!");
                        return;
                    }
                    conn.p.myclan.member_contribute_ngoc(conn, quant);
                }
                break;
            }
            case 10: {
                if (Manager.gI().event == 1) {
                    if (size != 1) {
                        return;
                    }
                    String value = m2.reader().readUTF();
                    if (!(Util.isnumber(value))) {
                        Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                        return;
                    }
                    int quant = Integer.parseInt(value);
                    if (quant > 500 || quant <= 0) {
                        Service.send_notice_box(conn, "Số lượng không hợp lệ!");
                        return;
                    }
                    //
                    if (conn.p.get_vang() < (quant * 20_000)) {
                        Service.send_notice_box(conn, "Vàng không đủ!");
                        return;
                    }
                    short[] id = new short[]{118, 119, 120, 121, 122};
                    for (int i = 0; i < id.length; i++) {
                        if (conn.p.item.total_item_by_id(4, id[i]) < (quant * 50)) {
                            Service.send_notice_box(conn, (ItemTemplate4.item.get(id[i]).getName() + " không đủ!"));
                            return;
                        }
                    }
                    conn.p.update_vang(-(quant * 20_000));
                    for (int i = 0; i < id.length; i++) {
                        conn.p.item.remove(4, id[i], quant * 50);
                    }
                    Item47 it = new Item47();
                    it.category = 4;
                    it.id = (short) 158;
                    it.quantity = (short) quant;
                    conn.p.item.add_item_bag47(4, it);
                    //
                    conn.p.item.char_inventory(4);
                    conn.p.item.char_inventory(7);
                    conn.p.item.char_inventory(3);
                    //
                    Service.send_notice_box(conn, "Đổi thành công " + quant + " hộp đồ chơi");
                }
                break;
            }
            case 11: {
                if (Manager.gI().event == 1) {
                    if (size != 1) {
                        return;
                    }
                    String value = m2.reader().readUTF();
                    if (!(Util.isnumber(value))) {
                        Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                        return;
                    }
                    int quant = Integer.parseInt(value);
                    if (quant > 500 || quant <= 0) {
                        Service.send_notice_box(conn, "Số lượng không hợp lệ!");
                        return;
                    }
                    //
                    short[] id = new short[]{153, 154, 155, 156};
                    for (int i = 0; i < id.length; i++) {
                        if (conn.p.item.total_item_by_id(4, id[i]) < (quant)) {
                            Service.send_notice_box(conn, (ItemTemplate4.item.get(id[i]).getName() + " không đủ!"));
                            return;
                        }
                    }
                    for (int i = 0; i < id.length; i++) {
                        conn.p.item.remove(4, id[i], quant);
                    }
                    Event_1.add_material(conn.p.name, quant);
                    //
                    conn.p.item.char_inventory(4);
                    conn.p.item.char_inventory(7);
                    conn.p.item.char_inventory(3);
                    //
                    Service.send_notice_box(conn, "Đóng góp nguyên liệu tạo " + quant + " kẹo");
                }
                break;
            }
            case 12: {
                if (Manager.gI().event == 1) {
                    if (size != 1) {
                        return;
                    }
                    String value = m2.reader().readUTF();
                    if (!(Util.isnumber(value))) {
                        Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                        return;
                    }
                    int quant = Integer.parseInt(value);
                    if (quant > 500 || quant <= 0) {
                        Service.send_notice_box(conn, "Số lượng không hợp lệ!");
                        return;
                    }
                    //
                    if (conn.p.get_vang() < (quant * 50_000)) {
                        Service.send_notice_box(conn, "Vàng không đủ!");
                        return;
                    }
                    if (conn.p.item.total_item_by_id(4, 162) < (quant * 5)) {
                        Service.send_notice_box(conn, (ItemTemplate4.item.get(162).getName() + " không đủ!"));
                        return;
                    }
                    conn.p.update_vang(-(quant * 50_000));
                    conn.p.item.remove(4, 162, quant * 5);
                    //
                    Item47 it = new Item47();
                    it.category = 4;
                    it.id = 157;
                    it.quantity = (short) quant;
                    conn.p.item.add_item_bag47(4, it);
                    //
                    conn.p.item.char_inventory(4);
                    conn.p.item.char_inventory(7);
                    conn.p.item.char_inventory(3);
                    //
                    Service.send_notice_box(conn, "Đổi thành công " + quant + " túi kẹo");
                }
                break;
            }
            case 13: {
                if (size != 1) {
                    return;
                }
                String name = m2.reader().readUTF();
                Pattern p = Pattern.compile("^[a-zA-Z0-9]{6,10}$");
                if (!p.matcher(name).matches()) {
                    Service.send_notice_box(conn, "tên không hợp lệ, nhập lại đi!!");
                    return;
                }
                if (conn.p.myclan != null && !conn.p.myclan.mems.get(0).name.equals(name)) {

                    conn.p.name_mem_clan_to_appoint = name;
                    Service.send_box_input_yesno(conn, 113, "Xác nhận nhường thủ lĩnh cho " + name);
                }
                break;
            }
            case 14: {
                if (size != 1) {
                    return;
                }
                String value = m2.reader().readUTF();
                if (!(Util.isnumber(value))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int coin_exchange = Integer.parseInt(value);
                if (coin_exchange < 1000 || coin_exchange > 300_000) {
                    Service.send_notice_box(conn, "Chỉ có thể đổi tối thiểu là 1k và tối đa là 300k");
                    return;
                }
                if (conn.p.update_coin(-coin_exchange)) {
                    conn.p.update_vang((long) ((coin_exchange / 2) * 10_000));
                    conn.p.item.char_inventory(5);
                    Service.send_notice_box(conn, "Đổi thành công");
//                    Log.gI().add_log(conn.p.name,
//                                    "đổi coin sang ngọc " + text + " : " + Util.number_format(ngoc_up) + " ngọc");
                } else {
                    Service.send_notice_box(conn, "Thất bại xin hãy thử lại");
                }
                break;
            }
            case 15: {
                if (size != 1) {
                    return;
                }
                String value = m2.reader().readUTF();
                if (!(Util.isnumber(value))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int quant = Integer.parseInt(value);
                if (quant > 32_000 || quant <= 0) {
                    Service.send_notice_box(conn, "Số lượng không hợp lệ!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Hành trang đầy!");
                    return;
                }
                int quant_ngoc_can_create = conn.p.item.total_item_by_id(7, conn.p.id_hop_ngoc) / 5;
                if (quant > quant_ngoc_can_create) {
                    Service.send_notice_box(conn, "Số lượng trong hành trang không đủ!");
                    return;
                }
                int vang_required = GameSrc.get_vang_hopngoc(conn.p.id_hop_ngoc) * quant;
                if (conn.p.get_vang() < vang_required) {
                    Service.send_notice_box(conn, "Không đủ " + vang_required + " vàng");
                    return;
                }
                conn.p.update_vang(-vang_required);
                conn.p.item.remove(7, conn.p.id_hop_ngoc, (quant * 5));
                Item47 itbag = new Item47();
                itbag.id = (short) (conn.p.id_hop_ngoc + 1);
                itbag.quantity = (short) quant;
                itbag.category = 7;
                conn.p.item.add_item_bag47(7, itbag);
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(3);
                conn.p.id_hop_ngoc = -1;
                //
                Message m = new Message(-100);
                m.writer().writeByte(3);
                m.writer().writeUTF("Nhận được " + quant + " " + ItemTemplate7.item.get(itbag.id).getName());
                m.writer().writeShort(itbag.id);
                m.writer().writeByte(7);
                conn.addmsg(m);
                m.cleanup();
                break;
            }
            case 17: {
                if (size != 1) {
                    return;
                }
                String vang_join = m2.reader().readUTF();
                if (!(Util.isnumber(vang_join))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int vang_join_vxmm = Integer.parseInt(vang_join);
                if (vang_join_vxmm < 500 || conn.p.get_ngoc() < vang_join_vxmm) {
                    Service.send_notice_box(conn, "ngọc không đủ!");
                    return;
                }
                if (vang_join_vxmm > 50_000) {
                    Service.send_notice_box(conn, "tối đa 50k ngọc!");
                    return;
                }
                Manager.gI().vxkc.join_vxmm(conn.p, vang_join_vxmm);
                break;
            }
            case 18: {
                if (conn.ac_admin <= 3) {
                    Service.send_notice_box(conn, "Không đủ thẩm quyền!");
                    return;
                }
                String nameUser = m2.reader().readUTF();
                //Pattern p = Pattern.compile("^[a-zA-Z0-9@.]{1,15}$");
//                if ( !p.matcher(nameUser).matches() ) {
//                    Service.send_notice_box(conn,"ký tự nhập vào không hợp lệ!!");
//                    return;
//                }
                for (int i = Session.client_entrys.size() - 1; i >= 0; i--) {
                    Session s = Session.client_entrys.get(i);
                    if (s != null && s.p != null && s.p.name != null && s.p.name.equals(nameUser)) {
                        Session.client_entrys.get(i).p.timeBlockCTG = Helps._Time.GetTimeNextDay();
                        Service.send_notice_box(conn, "Khóa mõm nhân vật " + nameUser + " 1 ngày thành công.");
                        return;
                    }
                }
                Service.send_notice_box(conn, "Không tìm thấy nhân vật hoặc không online");

                break;
            }
            case 19: {
                if (conn.ac_admin <= 3) {
                    Service.send_notice_box(conn, "Không đủ thẩm quyền!");
                    return;
                }
                String nameUser = m2.reader().readUTF();
                for (int i = Session.client_entrys.size() - 1; i >= 0; i--) {
                    Session s = Session.client_entrys.get(i);
                    if (s != null && s.p != null && s.p.name != null && s.p.name.equals(nameUser)) {
                        Session.client_entrys.get(i).p.timeBlockCTG = 0;
                        Service.send_notice_box(conn, "Đã gỡ mõm nhân vật " + nameUser);
                        return;
                    }
                }
                break;
            }
            case 20: {
                String namePlayer = m2.reader().readUTF();
                conn.p.Store_Sell_ToPL = namePlayer;
                Service.send_notice_box(conn, "Đã cài đặt chỉ bán cho nhân vật " + namePlayer);
                break;
            }
            case 21: {
                if (conn.ac_admin > 3) {
                    if (size != 3) {
                        return;
                    }
                    try {
                        Vgo v = new Vgo();
                        v.id_map_go = Byte.parseByte(m2.reader().readUTF());
                        v.x_new = Short.parseShort(m2.reader().readUTF());
                        v.y_new = Short.parseShort(m2.reader().readUTF());
                        conn.p.change_map(conn.p, v);
                    } catch (Exception e) {
                        Service.send_notice_box(conn, "Đã xảy ra lỗi!");
                    }

                }
                break;
            }
            case 22: {
                if (size != 1) {
                    return;
                }
                String thue = m2.reader().readUTF();
                if (!(Util.isnumber(thue))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int thuechange = Byte.parseByte(thue);
                if (thuechange < 0 || thuechange > 5) {
                    Service.send_notice_box(conn, "Chỉ có thể cài đặt thuế trong khoảng từ 0 đến 5%");
                    return;
                }
                if (conn.p.myclan == null || Manager.ClanThue == null || !conn.p.myclan.equals(Manager.ClanThue)) {
                    Service.send_notice_box(conn, "Chỉ clan chiếm được thành mới có thể đặt thuế!");
                } else if (!Manager.ClanThue.mems.get(0).name.equals(conn.p.name)) {
                    Service.send_notice_box(conn, "Chỉ chủ bang mới có quyền thực hiện hành động này!");
                } else {
                    Manager.thue = (byte) thuechange;
                    Service.send_notice_box(conn, "Bạn đã thay đổi mức thuế lên " + Manager.thue + " %");
                }
                break;
            }
            case 23: {
                String[] value = new String[]{m2.reader().readUTF(), m2.reader().readUTF()};
                if (!value[0].equals("") && !value[1].equals("")) {
                    // Service.send_notice_box(conn, "Bạn đã đặt tên bang là \"" + value[0] + "\" và tên viết tắt là \""
                    // + value[1] + "\" đúng không?\n đang test thôi nên éo có bang đâu kkk :v");
                    if (value[0].contains("_") || value[0].contains("-") || value[0].contains("@") || value[0].contains("#")
                            || value[0].contains("^") || value[0].contains("$") || value[0].length() > 20
                            || value[0].length() < 4) {
                        Service.send_notice_box(conn, "Tên nhập vào không hợp lệ");
                        return;
                    }
                    Pattern p = Pattern.compile("^[a-zA-Z0-9]{3,3}$");
                    if (!p.matcher(value[1]).matches()) {
                        Service.send_notice_box(conn, "Tên rút gọn nhập vào không hợp lệ");
                        return;
                    }
                    if (conn.p.get_ngoc() < 20000) {
                        Service.send_notice_box(conn, "Không đủ 20k ngọc!");
                        return;
                    }
                    if (Clan.create_clan(conn, value[0], value[1])) {
                        conn.p.update_ngoc(-20000);
                        Log.gI().add_log(conn.p.name, "Tạo bang mất 20000 ngọc");
                        conn.p.item.char_inventory(5);
                        Service.send_box_UI(conn, 20);
                        Service.send_notice_box(conn, "Hãy chọn một icon bất kỳ đặt làm biểu tượng");
                    }
                } else {
                    Service.send_notice_box(conn, "bỏ ô trống thì tạo bang cho mày thế éo nào đc hả?");
                }
                break;
            }
            case 24: {
                if (conn.ac_admin <= 3) {
                    Service.send_notice_box(conn, "Không đủ thẩm quyền!");
                    return;
                }
                try {
                    String type = m2.reader().readUTF();
                    String nameUser = m2.reader().readUTF();
                    if (type == null || type.isEmpty() || nameUser == null || nameUser.isEmpty()) {
                        Service.send_notice_box(conn, "Không được bỏ trống trường dữ liệu!");
                        return;
                    }
                    int count = 0;
                    switch (type) {
                        case "1":
                            for (int i = Session.client_entrys.size() - 1; i >= 0; i--) {
                                Session s = Session.client_entrys.get(i);
                                if (s != null && s.user != null && s.user.toLowerCase().equals(nameUser.toLowerCase())) {
                                    count++;
                                    System.out.println("=============close session " + s.user);
                                    Session.client_entrys.get(i).close();
                                }
                            }
                            Service.send_notice_box(conn, "Đã disconnect " + count + " session có tên tài khoản: " + nameUser);
                            break;
                        case "2":
                            for (int i = Session.client_entrys.size() - 1; i >= 0; i--) {
                                Session s = Session.client_entrys.get(i);
                                if (s != null && s.p != null && s.p.name != null && s.p.name.toLowerCase().equals(nameUser.toLowerCase())) {
                                    count++;
                                    System.out.println("=============close session " + s.user);
                                    Session.client_entrys.get(i).close();
                                }
                            }
                            Service.send_notice_box(conn, "Đã disconnect " + count + " session có tên nhân vật: " + nameUser);
                            break;
                        default:
                            Service.send_notice_box(conn, "Không đúng định dạng loại:\n1: Tên tài khoản \n2: Tên nhân vật");
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                    Service.send_notice_box(conn, "lỗi cmnr3!");
                }

                break;
            }
            case 25:
            case 26:
            case 27: {
                String value = m2.reader().readUTF();
                if (!(Util.isnumber(value))) {
                    Service.send_notice_box(conn, "Dữ liệu nhập không phải số!!");
                    return;
                }
                int quant = Integer.parseInt(value);
                if (quant > 200 || quant <= 0) {
                    Service.send_notice_box(conn, "Số lượng không hợp lệ!");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Hành trang đầy!");
                    return;
                }
                short id_cu = 306, id_moi = 307, chuyendoi = 30;
                long vag = quant * 100_000;
                if (idnpc == 26) {
                    id_cu = 306;
                    id_moi = 304;
                    chuyendoi = 10;
                    vag = quant * 25_000;
                } else if (idnpc == 27) {
                    id_cu = 304;
                    id_moi = 305;
                    chuyendoi = 5;
                    vag = quant * 500;
                }
                if (idnpc == 27 && vag > conn.p.get_ngoc()) {
                    Service.send_notice_box(conn, "Không đủ " + vag + " ngọc để đổi " + quant + " bó sen");
                    return;
                } else if (vag > conn.p.get_vang()) {
                    Service.send_notice_box(conn, "Không đủ " + vag + " vàng để đổi " + quant + " bó sen");
                    return;
                }
                if (id_cu > (ItemTemplate4.item.size() - 1) || id_cu < 0 || id_moi > (ItemTemplate4.item.size() - 1) || id_moi < 0) {
                    Service.send_notice_box(conn, "Đã xảy ra lỗi...");
                    return;
                }
                int quant_inbag = conn.p.item.total_item_by_id(4, id_cu);
                int quant_real = quant_inbag / chuyendoi;
                if (quant_real < quant) {
                    Service.send_notice_box(conn, "Chỉ có thể đổi tối đa " + quant_real + " " + ItemTemplate4.item.get(id_moi).getName());
                    return;
                }

                if (idnpc == 27) {
                    conn.p.update_ngoc(-(vag));
                } else {
                    conn.p.update_vang(-(vag));
                }
                Item47 itbag = new Item47();
                itbag.id = id_moi;
                itbag.quantity = (short) quant;
                itbag.category = 4;
                conn.p.item.remove(4, id_cu, quant * chuyendoi);
                conn.p.item.add_item_bag47(4, itbag);
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(5);

                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id_moi}, new int[]{quant}, new short[]{4});
                break;
            }
            case 28: {
                String namep = m2.reader().readUTF();
                Player p0 = null;
                for (Player p1 : conn.p.map.players) {
                    if (p1.conn != null && p1.conn.connected && p1.name.equals(namep) && Math.abs(conn.p.x - p1.x) < 70 && Math.abs(conn.p.y - p1.y) < 70) {
                        p0 = p1;
                        break;
                    }
                }
                if (p0 == null) {
                    Service.send_notice_box(conn, "Bạn và người thả cùng cần phải đứng gần nhau");
                    break;
                }
                if (conn.p.item.get_bag_able() < 3) {
                    Service.send_notice_box(conn, "Cần 3 ô trống trong hành trang!");
                    return;
                }
                if (conn.p.item.total_item_by_id(4, 303) > 0) {
//                    try{
                    conn.p.item.remove(4, 303, 1);
                    List<box_item_template> ids = new ArrayList<>();

                    List<Integer> it7 = new ArrayList<>(java.util.Arrays.asList(12, 13, 11, 3, 4, 8, 9, 10));
                    List<Integer> it7_vip = new ArrayList<>(java.util.Arrays.asList(14, 471, 346, 33));

                    List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList(294, 275, 52, 18));
                    List<Integer> it4_vip = new ArrayList<>(java.util.Arrays.asList(206, 147, 131, 304, 306));
                    for (int i = 0; i < Util.random(1, 4); i++) {
                        int ran = Util.random(100);
                        if (ran < 0) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 2) { //sách
                            short idsach = (short) Util.random(4577, 4585);
                            ids.add(new box_item_template(idsach, (short) 1, (byte) 3));
                            conn.p.item.add_item_bag3_default(idsach, 0, false);
                        } else if (ran < 5) {//nlmd vang tim
                            short id = (short) Util.random(126, 146);
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 15) { // nltt
                            short id = (short) Util.random(417, 464);
                            short quant = (short) Util.random(2);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 28) {
                            short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 45) {
                            short id = Util.random(it4_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 70) {
                            short id = Util.random(it4, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        }
                    }
                    ev_he.Event_3.add_DoiQua(conn.p.name, 1);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
//                    }catch(Exception e){e.printStackTrace();}
                }
                
                
                if(p0.item.get_bag_able()<3){
                    Service.send_notice_box(p0.conn, "Cần 3 ô trống trong hành trang để có thể nhận quà hoa đăng từ "+conn.p.name);
                    return;
                }
                
                List<box_item_template> ids = new ArrayList<>();

                List<Integer> it7 = new ArrayList<>(java.util.Arrays.asList( 1,2,3));
                List<Integer> it7_vip = new ArrayList<>(java.util.Arrays.asList( 12,8,9,10));
                List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList( 48,49,50,51,18,10));
                List<Integer> it4_vip = new ArrayList<>(java.util.Arrays.asList(205, 207,24,52,275,84));
                for(int i=0; i< Util.random(1, 3); i++){
                    int ran = Util.random(100);
                    if(ran<0){
                        short id = Util.random(it7,new ArrayList<>()).shortValue();
                        short quant = (short) Util.random(2,5);
                        ids.add(new box_item_template(id, quant, (byte)7));
                        p0.item.add_item_bag47(id,quant,(byte)7);
                    }
                    else if(ran < 2){ // nltt
                        short id = (short) Util.random(417, 464);
                        short quant = (short) Util.random(2);
                        ids.add(new box_item_template(id, quant, (byte)7));
                        p0.item.add_item_bag47(id,quant,(byte)7);
                    }
                    else if(ran < 12){
                        short id = Util.random(it4_vip,new ArrayList<>()).shortValue();
                        short quant = (short) 1;
                        ids.add(new box_item_template(id, quant, (byte)4));
                        p0.item.add_item_bag47(id,quant,(byte)4);
                    }
                    else if(ran < 27){
                        short id = Util.random(it7_vip,new ArrayList<>()).shortValue();
                        short quant = (short) 1;
                        ids.add(new box_item_template(id, quant, (byte)7));
                        p0.item.add_item_bag47(id,quant,(byte)7);
                    }
                    else if(ran < 45){
                        short id = Util.random(it4,new ArrayList<>()).shortValue();
                        short quant = (short) Util.random(1,3);
                        ids.add(new box_item_template(id, quant, (byte)4));
                        p0.item.add_item_bag47(id,quant,(byte)4);
                    }
                    else if(ran < 70){
                        short id = Util.random(it7,new ArrayList<>()).shortValue();
                        short quant = (short) Util.random(1,3);
                        ids.add(new box_item_template(id, quant, (byte)7));
                        p0.item.add_item_bag47(id,quant,(byte)7);
                    }
                    else{
                        short id = (short)Util.random(new int[]{2,5});
                        short quant = (short) Util.random(100,300);
                        ids.add(new box_item_template(id, quant, (byte)4));
                        p0.item.add_item_bag47(id,quant,(byte)4);
                    }
                }
                Service.Show_open_box_notice_item(p0, "Quà hoa đăng từ "+conn.p.name, ids);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Đã xảy ra lỗi");
                break;
            }
        
    
        } 
    }
        }