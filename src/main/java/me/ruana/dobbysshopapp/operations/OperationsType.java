package me.ruana.dobbysshopapp.operations;

public enum OperationsType {
    PUT_SOCKS("Приход носков на склад"),
    DELETE_SOCKS ("Списание брака"),
    SELL_SOCKS ("Продажа носков");
    private final String operationType;

    OperationsType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationType() {
        return operationType;
    }

    @Override
    public String toString() {
        return name() + ": ";
    }
}
