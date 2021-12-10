package iuh.ad.a19489471_dinhtuananh_ad_todoapp;

public class CongViec {
    private int IdTask;
    private String NameTask;

    public CongViec(int idCV, String tenCV) {
        IdTask = idCV;
        NameTask = tenCV;
    }

    public int getIdCV() {
        return IdTask;
    }

    public void setIdCV(int idCV) {
        IdTask = idCV;
    }

    public String getTenCV() {
        return NameTask;
    }

    public void setTenCV(String tenCV) {
        NameTask = tenCV;
    }
}