package user;

public enum Authority {
	Superuser(3), System_Administrator(2), Viewer(1), None(0);

    private final int level;

    Authority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
}
