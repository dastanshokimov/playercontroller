package models;

public class PlayerDeleteRequestDto {
    private long playerId;

    public long getPlayerId() {
        return playerId;
    }

    public void setId(long playerId) {
        this.playerId = playerId;
    }

    public PlayerDeleteRequestDto(long playerId) {
        this.playerId = playerId;
    }
}
