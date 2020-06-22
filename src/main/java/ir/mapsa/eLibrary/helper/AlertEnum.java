package ir.mapsa.eLibrary.helper;

public enum AlertEnum {
    SUCCESS("success"), ERROR("error");

    private String name;

    AlertEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
