package P3.Backend.DB_Testing;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();

        db.addDiagnostics("MyIDMaybe", true, 2, 3, 4, 2, "Yes", "No", "WhyMe?");
    }
}
