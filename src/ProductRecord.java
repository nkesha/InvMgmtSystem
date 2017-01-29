class ProductRecord
{
    String productCode;
    String category;
    String description;
    int aisle;
    int currInventory;

    // Constructor.
    public ProductRecord (String productCode, String category,
                          String description,
                          int aisle,
                          int currInventory)
    {
        this.productCode = productCode;
        this.category = category;
        this.description = description;
        this.aisle = aisle;
        this.currInventory = currInventory;

    }
    public String toString()
    {
        return "Code: " + productCode + "(" + category + ") " + description +
                "; Aisle " + aisle +
                " (have " + currInventory + ")";
    }
}