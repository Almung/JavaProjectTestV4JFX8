package model;

import java.util.UUID;

public class Locataire {
    private UUID id;
    private String name;
    private String email;
    private String phone;

    public Locataire(String name, String email, String phone) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return String.format("%s - %s - %s", name, email, phone);
    }
}