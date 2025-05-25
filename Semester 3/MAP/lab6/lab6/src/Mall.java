public class Mall {
    public String name;
    public String manager;
    public int year;
    public double profit;
    public Mall(String name, String manager, int year, double profit) {
        this.name = name;
        this.manager = manager;
        this.year = year;
        this.profit = profit;
    }

    public double getProfit() {
        return profit;
    }
    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getName() {
        return name;
    }

    public String getManager() {
        return manager;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Mall {"+name+", "+manager+", "+year+", "+profit+"}";
    }
}
