package template;

public class Option {

    private static final int[] parafterupdate
            = new int[]{1, 110, 120, 130, 140, 150, 160, 172, 184, 196, 208, 220, 235, 250, 265, 280};
    public byte id;
    private int param;
    private short idItem;

    public Option(int id, int param, short iditem) {
        this.id = (byte) id;
        this.param = param;
        this.idItem = iditem;
    }
//        public Option(int id, int param) {
//		this.id = (byte) id;
//		this.param = param;
//	}

    public Option(int id, int param) {
		this.id = (byte) id;
		this.param = param;
	}

    public int getParam(int tier) {
        if ((id >= 100 && id <= 107)||(id >= 58 && id<=60)) {
            return param;
        }
        if (Helps.CheckItem.isMeDay(idItem)) {
            return getParamMD(tier);
        }
        //return param;
        if (tier == 0) {
            return param;
        }
        //
        int parbuffer = this.param;

        if (this.id >= 29 && this.id <= 36 || this.id >= 16 && this.id <= 22 || this.id == 41) {
            parbuffer += 20 * tier;
            return parbuffer;
        }

        if (this.id >= 23 && this.id <= 26) {
            return (parbuffer + tier);
        }
        if (this.id == 42) {
            return (parbuffer + tier * 400);
        }
        if ((this.id >= 7 && this.id <= 13) || this.id == 15 || this.id == 27 || this.id == 28) {
            return (parbuffer + 100 * tier);
        }
        if ((this.id == 37 || this.id == 38) && tier < 9) {
            return 1;
        }
        if (tier > 15) {
            tier = 15;
        }
        if ((this.id >= 0 && this.id <= 6) || this.id == 14 || this.id == 40) {
            parbuffer = (parafterupdate[tier] * this.param) / 100;
            return parbuffer;
        }
        return parbuffer;
    }

    public int getParamMD(int tier) {
//            if(tier>15)
//                System.out.println("==================="+tier);
        if (tier == 0) {
            return param;
        }
        if ((this.id == 37 || this.id == 38)) {
            return 1;
        }
        //
        int parbuffer = this.param;
        if (this.id >= 0 && this.id <= 6) {
            return (parbuffer + ((int) (parbuffer * tier * 0.33)));
        }

        if (this.id == 81 || this.id == 86 || this.id == 88 || this.id == 77 || this.id == 79) // giây dòng vip
        {
            return (int) (parbuffer * tier * 0.5);
        }
        if (this.id == 85 || this.id == 87 || this.id == 80 || this.id == 82) // dòng vip
        {
            return (int) (parbuffer * tier * 0.07);
        }
        if (this.id == 78 || this.id == 76) // dòng vip
        {
            return (int) (parbuffer * tier * 0.1);
        }

        if ((this.id >= 76 && this.id <= 89) || this.id == 97 || this.id == 98 || this.id == 95) // dòng vip
        {
            return (int) (parbuffer * tier * 0.07);
        }
        if (this.id >= 29 && this.id <= 36 || this.id >= 16 && this.id <= 22 || this.id == 41) {
            parbuffer += 50 * tier;
            return parbuffer;
        }

        if (this.id >= 23 && this.id <= 26) {
            return (parbuffer + tier);
        }
        if (this.id == 42) {
            return (parbuffer + tier * 400);
        }
        if ((this.id >= 7 && this.id <= 13)) {
            return (this.param * tier);
        }
        if (this.id == 15 || this.id == 27 || this.id == 28) {
            return (parbuffer + 100 * tier);
        }

        if (tier > 15) {
            tier = 15;
        }
        if ((this.id >= 0 && this.id <= 6) || this.id == 14 || this.id == 40) {
            parbuffer = (parafterupdate[tier] * this.param) / 100;
            return parbuffer;
        }
        return parbuffer;
    }

    public void setParam(int param) {
        this.param = param;
    }
}
