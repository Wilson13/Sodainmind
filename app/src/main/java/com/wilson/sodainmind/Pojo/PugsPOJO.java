package com.wilson.sodainmind.Pojo;

public class PugsPOJO
{
    private String[] pugs;

    public String[] getPugs ()
    {
        return pugs;
    }

    public void setPugs (String[] pugs)
    {
        this.pugs = pugs;
    }

    @Override
    public String toString()
    {
        return "PugsPOJO [pugs = "+pugs+"]";
    }
}
