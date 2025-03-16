class LeaderboardEntry {
    int id;
    String user_name;
    String game_name;
    int score;
    String achievements;

    LeaderboardEntry(int id, String user_name ,String game_name, int score, String achievements) {
        this.id = id;
        this.user_name = user_name;
        this.game_name = game_name;
        this.score = score;
        this.achievements = achievements;
    }
}
