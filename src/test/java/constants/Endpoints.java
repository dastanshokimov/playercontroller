package constants;

public class Endpoints {
    // Create a new player (editor = supervisor/admin, etc.)
    public static final String CREATE_PLAYER = "/player/create/{editor}";

    // Delete player by playerId (editor required)
    public static final String DELETE_PLAYER = "/player/delete/{editor}";

    // Get player by playerId (playerId in request body)
    public static final String GET_PLAYER_BY_ID = "/player/get";

    // Get all players
    public static final String GET_ALL_PLAYERS = "/player/get/all";

    // Update player (editor and playerId required)
    public static final String UPDATE_PLAYER = "/player/update/{editor}/{id}";
}
