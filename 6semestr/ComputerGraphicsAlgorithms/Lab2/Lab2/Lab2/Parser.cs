using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Numerics;
using System.Globalization;

namespace Lab1Wpf
{
    internal class Parser
    {
        static string[] separatingText = { "\n" };
        static  string[] separatingLine = { " " };
        static string[] separatingSlash = { "/" };
        public static Model Parse(string data)
        {
            string[] Lines = data.Split(separatingText, System.StringSplitOptions.RemoveEmptyEntries);

            Model model = new Model();
            List<Vector4> Pnts = new List<Vector4>();

            List<List<int>> Poligons = new List<List<int>>();

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

                        if (Words[0] == "f")
                        {
                            List<int> Pol = new List<int>(); ;

                            foreach (string word in Words)
                            {
                                if (word != "f")
                                {
                                    string[] Numbers = word.Split(separatingSlash, System.StringSplitOptions.RemoveEmptyEntries);

                                    try
                                    {
                                        Pol.Add(int.Parse(Numbers[0])-1);
                                    }
                                    catch { 
                                    }
                                }
                            }
                            Poligons.Add(Pol);
                        }
                    }
                }

            }

            model.SetPoints(Pnts);
            model.SetPoligons(Poligons);

            return model;
        }
    }
}
