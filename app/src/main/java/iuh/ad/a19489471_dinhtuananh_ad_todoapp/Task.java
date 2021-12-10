package iuh.ad.a19489471_dinhtuananh_ad_todoapp;

public class Task {
    private int IdTask;
    private String NameTask;

    public Task(int id, String nameTask) {
        IdTask = id;
        NameTask = nameTask;
    }

    public int getId() {
        return IdTask;
    }

    public void setId(int id) {
        IdTask = id;
    }

    public String getNameTask() {
        return NameTask;
    }

    public void setNameTask(String nameTask) {
        NameTask = nameTask;
    }
}