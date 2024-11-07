using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing.Imaging;
using System.Drawing;

namespace Lab3Net
{
    internal class JPGImage
    {
        private Bitmap image;
        private byte[] imageData;
        public int width;
        public int height;
        private int bytesPerPixel;

        public JPGImage(string filePath)
        {
            // Загрузка изображения
           image = new Bitmap(filePath);

            // Обработка изображения

            // Получаем размеры изображения
            this.width = image.Width;
            this.height = image.Height;

        }

        public byte[] GetPixelColor(int x, int y)
        {
            Color pixel = new Color();
            lock (image)
            {
                if(x<width  && y<height && y>=0)
                pixel = image.GetPixel(x, y);
            }
           
            return new byte[] { pixel.B, pixel.G, pixel.R };
        }
    }
}
