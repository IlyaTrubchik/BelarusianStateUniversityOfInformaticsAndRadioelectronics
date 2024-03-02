using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

namespace Lab1Wpf
{
    public static class VectorExtension
    {
        public static Vector4 normalize(this Vector4 vector)
        {
            float length = (float)Math.Sqrt(vector.X * vector.X + vector.Y * vector.Y + vector.Z * vector.Z);
            vector.X = (float)(vector.X / length);
            vector.Y = (vector.Y / length);
            vector.Z = vector.Z / length;
            return vector;
        }
    }
}
