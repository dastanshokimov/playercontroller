package models;

import java.util.List;

public class AllPlayersDto {
    private List<PlayerItem> players;

    public List<PlayerItem> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerItem> players) {
        this.players = players;
    }
}
