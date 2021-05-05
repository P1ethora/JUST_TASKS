package port213.ship.shipcontent;

public enum WaybillField {

    LOAD,
    UNLOAD,
    TRANSFER;

    public static WaybillField getRandom() {
        return values()[(int) (Math.random() * values().length)];

    }
}
