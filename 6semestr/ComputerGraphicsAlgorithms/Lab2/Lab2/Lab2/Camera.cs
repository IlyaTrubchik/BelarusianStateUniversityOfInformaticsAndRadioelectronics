using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Numerics;

namespace Lab1Wpf
{
    internal class Camera
    {
        Vector4 target;
        Vector4 position;
        Vector4 up;
        float tethaAngle;
        float phiAngle;
        float r;
       
        public Camera(Vector4 target,Vector4 position)
        {
            this.target = target;
            this.position = position;
            up = new Vector4();
            up.X = 0;
            up.Y = 1;
            up.Z = 0;
            up.W = 1;
            calculatePhi();
            calculateTetha();
            calculateR();
        }
        public void rotateY(float mouseDelta,float height)
        {
            this.tethaAngle -= (mouseDelta / height) * 2 * (float)Math.PI;
            //float angle = (mouseDelta / height) * 2 * (float)Math.PI;
            //MatrixTransformations.rotateZ((mouseDelta / height) * 2 * (float)Math.PI, ref this.position);
            calculatePosition();
        }
  
        public void rotateZ(float mouseDelta,float width)
        {
            this.phiAngle -= (mouseDelta / width) * 2 * (float)Math.PI;
            //MatrixTransformations.rotateY((mouseDelta / width) * 2 * (float)Math.PI, ref this.position);
            calculatePosition();
        }
        private void calculatePosition()
        {
            this.position.Z = this.r * (float)Math.Cos(phiAngle) * (float)Math.Sin(tethaAngle);
            this.position.X = this.r * (float)Math.Sin(phiAngle) * (float)Math.Sin(tethaAngle);
            this.position.Y = this.r * (float)Math.Cos(tethaAngle);
            calculateR();
        }
        private void calculateTetha()
        {
            this.tethaAngle = (float)Math.Atan(Math.Sqrt(position.Z * position.Z + position.X * position.X) / position.Y);
        }
        private void calculatePhi()
        {
            this.phiAngle = (float)Math.Atan(position.X / position.Z);
        }
        private void calculateR()
        {
            this.r = (float)Math.Sqrt(position.X * position.X + position.Y * position.Y + position.Z * position.Z);
        }
        public Vector4 getPosition()
        {
            return this.position;
        }
        public Vector4 getTarget()
        {
            return this.target;
        }
        public Vector4 getUp()
        {
            return this.up;
        }
    }
}
