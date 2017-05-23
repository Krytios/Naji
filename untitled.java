 public class Employe {
    
    private int id;
    private String nom;
    private String prenom;
    private int age;
    private int salaire;
    
    public Employe(int ID, String FirstName, String LastName, int Age, int salaire)
    {
        this.id = ID;
        this.firstName = FirstName;
        this.lastName = LastName;
        this.age = Age;
        this.salaire = salaire;
    }
    
    public int getId()
    {
        return id;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public String getLastNAme()
    {
        return lastName;
    }
    
    public int getAge()
    {
        return age;
    }
    public int getSalaire()
    {
        return salaire;
    }
}
