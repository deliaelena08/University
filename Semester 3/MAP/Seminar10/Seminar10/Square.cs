namespace Seminar10;

public class Square : Shape
{
    private double side;
    
    public Square(double side)
    {
        this.side = side;
    }
    
    public double Side
    {
        get { return side; }
        set { side = value; }
    }
    
    public double computeArea()
    {
        return side * side;
    }

    public override string ToString()
    {
        return "Square with side: " + side;
    }
}