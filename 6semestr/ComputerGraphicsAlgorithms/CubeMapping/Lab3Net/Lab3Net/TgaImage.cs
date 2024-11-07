using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System;
using System.IO;
using SixLabors;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;

namespace Lab1Wpf
{
  

    class TgaImage
    {
        Image<Rgb24> image;
        private byte[] imageData;
        public int width;
        public int height;
        private int bytesPerPixel;

        public TgaImage(string filePath)
        {
            // Загрузка изображения
            Image<Rgb24> image = Image.Load<Rgb24>(filePath);
            
                // Обработка изображения

                // Получаем размеры изображения
                this.image = image;
                this.width = image.Width;
                this.height = image.Height;
            
        }

        public byte[] GetPixelColor(int x, int y)
        {
            Rgb24 pixel = image[x, height-y];
            if(x<500)
            {
                Console.WriteLine("сюда");
            }else
            {
                Console.WriteLine("туда");
            }
            return new byte[] { pixel.B, pixel.G,  pixel.R };
        }
    }
}
