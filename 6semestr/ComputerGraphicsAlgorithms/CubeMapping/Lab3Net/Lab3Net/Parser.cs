using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Numerics;
using System.Globalization;
using System.Windows;

namespace Lab1Wpf { 
internal class Parser
{
    static string[] separatingText = { "\n" };
    static string[] separatingLine = { " " };
    static string[] separatingSlash = { "/" };
    public static Model Parse(string data)
    {
        string[] Lines = data.Split(separatingText, System.StringSplitOptions.RemoveEmptyEntries);

        Model model = new Model();
        List<Vector4> Pnts = new List<Vector4>();
        List<Vector3> PN = new List<Vector3>();
        List<Vector3> PT = new List<Vector3>();

        List<List<int>> Poligons = new List<List<int>>();
        List<List<int>> PoligonsN = new List<List<int>>();
        List<List<int>> PoligonsT = new List<List<int>>();


        foreach (string s in Lines)
        {
            if (s != null)
            {
                string[] Words = (s.Trim()).Split(separatingLine, System.StringSplitOptions.RemoveEmptyEntries);

                int Size = Words.Length;

                if (Size > 0)
                {
                    if (Words[0] == "v")
                    {
                        Vector4 point = new Vector4();

                        point.X = float.Parse(Words[1], CultureInfo.InvariantCulture);
                        point.Y = float.Parse(Words[2], CultureInfo.InvariantCulture);
                        point.Z = float.Parse(Words[3], CultureInfo.InvariantCulture);
                        point.W = 1;

                        Pnts.Add(point);
                    }

                    if (Words[0] == "vn")
                    {
                        Vector3 point = new Vector3();

                        point.X = float.Parse(Words[1], CultureInfo.InvariantCulture);
                        point.Y = float.Parse(Words[2], CultureInfo.InvariantCulture);
                        point.Z = float.Parse(Words[3], CultureInfo.InvariantCulture);


                        PN.Add(point);
                    }

                    if (Words[0] == "vt")
                    {
                        Vector3 point = new Vector3();

                        point.X = float.Parse(Words[1], CultureInfo.InvariantCulture);
                        point.Y = float.Parse(Words[2], CultureInfo.InvariantCulture);
                        //point.Z = float.Parse(Words[3], CultureInfo.InvariantCulture);


                        PT.Add(point);
                    }

                    if (Words[0] == "f")
                    {
                        List<int> Pol = new List<int>();
                        List<int> PolN = new List<int>();
                        List<int> PolT = new List<int>();

                        foreach (string word in Words)
                        {
                            if (word != "f")
                            {
                                string[] Numbers = word.Split(separatingSlash, System.StringSplitOptions.RemoveEmptyEntries);

                                try
                                {
                                    Pol.Add(int.Parse(Numbers[0]) - 1);
                                }
                                catch
                                {
                                }

                                try
                                {
                                    PolT.Add(int.Parse(Numbers[1]) - 1);
                                }
                                catch
                                {
                                }

                                try
                                {
                                    PolN.Add(int.Parse(Numbers[2]) - 1);
                                }
                                catch
                                {
                                }

                            }
                        }
                        Poligons.Add(Pol);
                        PoligonsN.Add(PolN);
                        PoligonsT.Add(PolT);
                    }
                }
            }

        }

  //     MessageBox.Show(PoligonsT.Count.ToString());

        model.SetPoints(Pnts);
        model.SetPoligons(Poligons);

        model.SetPointsN(PN);
        model.SetPoligonsN(PoligonsN);

        model.SetPointsT(PT);
        model.SetPoligonsT(PoligonsT);


        return model;
    }
}
}

