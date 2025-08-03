package models;

public class PlayerGetRequestDto {
    private long playerId;

    public PlayerGetRequestDto() {
    }

    public PlayerGetRequestDto(long playerId) {
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
