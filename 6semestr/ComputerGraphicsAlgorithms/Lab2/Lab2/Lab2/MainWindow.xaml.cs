using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Numerics;
using System.IO;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Threading;
using System.Windows.Threading;



namespace Lab1Wpf
{
    /// <summary>
    /// Логика взаимодействия для MainWindow.xaml
    /// </summary>
    /// 

    public partial class MainWindow : Window
    {
        static object lockObject = new object();
        float yPrevMouse = 0;
        float xPrevMouse = 0;
        int imageWidth = 0;
        int imageHeight = 0;
        Camera camera;
        Model model = new Model();
        WriteableBitmap writeableBitmap;
        Vector4 currentScale = new Vector4();
        bool isPressed = false;
        int pixelSize;
        int stride;
        byte[] pixels;
        Vector4 lightPos;
        float[] zBuffer;
        bool wireFrameMode;

        public MainWindow()
        {
            InitializeComponent();
            Vector4 target = new Vector4(0, 5, 0, 1);
            Vector4 position = new Vector4(10, 0, 5, 1);
            camera = new Camera(target, position);
            currentScale.X = 1;
            currentScale.Y = 1;
            currentScale.Z = 1;
            MatrixTransformations.getProjectionMatrix((float)drawingImage.Width, (float)drawingImage.Height, 1, 1000);
            MatrixTransformations.getViewPortMatrix((float)drawingImage.Width, (float)drawingImage.Height, 0, 0);
            lightPos = new Vector4(0, 0, 1, 0);
            lightPos = camera.getPosition();
            wireFrameMode = false;
        }

        void render()
        {
            imageWidth = (int)drawingImage.Width;
            imageHeight = (int)drawingImage.Height;
            lightPos = camera.getPosition();
            lightPos = lightPos.normalize();

            Vector4[] points = new Vector4[model.GetPointCount()];
            MatrixTransformations.getCameraMatrix(camera.getPosition(), camera.getUp(), camera.getTarget());

            model.GetPoints().CopyTo(points, 0);
            //Преобразование координат
            MatrixTransformations.toWorld(currentScale, points);
            points.CopyTo(MatrixTransformations.worldModel, 0);
            MatrixTransformations.toCamera(points);
            MatrixTransformations.toProjection(points);
            points.CopyTo(MatrixTransformations.projectionPoints, 0);
            MatrixTransformations.cut(points);
            MatrixTransformations.toScreen(points);
            // Объявление массива для пикселей
            pixelSize = 4; // Каждый пиксель занимает 4 байта в формате Bgra32
            stride = imageWidth * pixelSize; // Длина одной строки пикселей
            int bufferSize = imageHeight * stride; // Общий размер буфера пикселей
            pixels = new byte[bufferSize];

            List<int>[] poly = checkPolygons(points).ToArray();

            if (!wireFrameMode)
            {
                zBuffer = Enumerable.Repeat(float.MaxValue, imageWidth * imageHeight).ToArray();
                Parallel.For(0, poly.Length, (i) =>
                {
                    List<int> face = poly[i];
                    for (int j = 2; j < face.Count; j++)
                    {
                        float intensity = getIntensity(face[0], face[j - 1], face[j]);
                        if (intensity > 0)
                        {
                            drawTriangleI(points, poly[i][0], poly[i][j - 1], poly[i][j], (byte)(255 * intensity), (byte)(255 * intensity), (byte)(255 * intensity));
                        }
                    }
                });
            }
            else
            {
                drawLines(points, poly);
            }

            //запись пикселей в битмап
            writeableBitmap = new WriteableBitmap((int)drawingImage.Width, (int)drawingImage.Height, 96, 96, PixelFormats.Bgr32, null);
            writeableBitmap.WritePixels(new Int32Rect(0, 0, (int)drawingImage.Width, (int)drawingImage.Height), pixels, stride, 0);
            drawingImage.Source = writeableBitmap;
        }

        private List<List<int>> checkPolygons(Vector4[] points)
        {
            List<List<int>> result = new List<List<int>>();
            List<int>[] allPolygons = model.getPolygons();
            for (int i = 0; i < allPolygons.Length; i++)
            {
                List<int> face = allPolygons[i];
                bool flag = true;
                for (int j = 0; j < face.Count; j++)
                {
                    if (points[face[j]].W == 0)
                    {
                        flag = false;
                        break;
                    }
                }
                if (flag)
                {
                    result.Add(face);
                }
            }
            return result;
        }
        private float getIntensity(int p1, int p2, int p3)
        {
            Vector4 normal = MatrixTransformations.vectorMultiply(Vector4.Subtract(MatrixTransformations.worldModel[p2], MatrixTransformations.worldModel[p1]),
                Vector4.Subtract(MatrixTransformations.worldModel[p3], MatrixTransformations.worldModel[p1]));
            Vector4 viewDir = Vector4.Subtract(camera.getPosition(), MatrixTransformations.worldModel[p1]);
            normal = normal.normalize();
            double a = Vector4.Dot(viewDir.normalize(), normal);
            float intensity = 0;
            if (a > 0)
            {
                intensity = (float)(Vector4.Dot(normal, lightPos) * 0.7);
            }
            return intensity;
        }
        private void drawPoint(int x, int y, byte colorA, byte colorB, byte colorC)
        {
            if (x <= imageWidth && y <= imageHeight && x > 0 && y > 0)
            {
                int pixelOffset = ((int)y - 1) * stride + ((int)x - 1) * pixelSize;
                pixels[pixelOffset + 0] = colorA; // Синий
                pixels[pixelOffset + 1] = colorB; // Зеленый
                pixels[pixelOffset + 2] = colorC; // Красный
                pixels[pixelOffset + 3] = 255; // Альфа-канал
            }
        }
        private void drawTriangleI(Vector4[] points, int i0, int i1, int i2, byte colorA, byte colorB, byte colocC)
        {
            Vector4 t0 = points[i0];
            Vector4 t1 = points[i1];
            Vector4 t2 = points[i2];
            t0.Y = (float)(Math.Round(t0.Y));
            t0.X = (float)(Math.Round(t0.X));
            t0.Z = MatrixTransformations.projectionPoints[i0].Z;

            t1.X = (float)(Math.Round(t1.X));
            t1.Y = (float)(Math.Round(t1.Y));
            t1.Z = MatrixTransformations.projectionPoints[i1].Z;

            t2.X = (float)(Math.Round(t2.X));
            t2.Y = (float)(Math.Round(t2.Y));
            t2.Z = MatrixTransformations.projectionPoints[i2].Z;

            if (t0.Y == t1.Y && t0.Y == t2.Y)
            {
                return;
            }
            if (t0.Y > t1.Y) Swap<Vector4>(ref t0, ref t1);
            if (t0.Y > t2.Y) Swap<Vector4>(ref t0, ref t2);
            if (t1.Y > t2.Y) Swap<Vector4>(ref t1, ref t2);
            int total_height = (int)((t2.Y) - (t0.Y));
            for (int i = 0; i < total_height; i++)
            {
                bool second_half = i > (t1.Y) - (t0.Y) || (t1.Y) == (t0.Y);
                int segment_height = second_half ? (int)((t2.Y) - (t1.Y)) : (int)((t1.Y) - (t0.Y));
                float alpha = (float)i / total_height;
                float beta = (float)(i - (second_half ? (t1.Y) - (t0.Y) : 0)) / segment_height;
                Vector4 A = Vector4.Add(t0, Vector4.Multiply(Vector4.Subtract(t2, t0), alpha));
                Vector4 B = second_half ? Vector4.Add(t1, Vector4.Multiply(Vector4.Subtract(t2, t1), beta)) : Vector4.Add(t0, Vector4.Multiply(Vector4.Subtract(t1, t0), beta));
                if ((A.X) > (B.X)) Swap<Vector4>(ref A, ref B);

                for (int j = (int)Math.Round(A.X); j <= (B.X); j++)
                {
                    float phi = (B.X) == (A.X) ? 1f : (float)(j - (A.X)) / (float)((B.X) - (A.X));

                    float currentZ = (A.Z) == (B.Z) ? A.Z : (float)(A.Z + phi * (B.Z - A.Z));
                    if (j <= imageWidth && ((int)((t0.Y) + i)) <= imageHeight && ((int)((t0.Y) + i)) > 0 && j > 0)
                    {
                        if (zBuffer[j - 1 + ((int)((t0.Y) + i) - 1) * imageWidth] > currentZ)
                        {
                            zBuffer[j - 1 + ((int)((t0.Y) + i) - 1) * imageWidth] = currentZ;
                            drawPoint(j, (int)((t0.Y) + i), colorA, colorB, colocC);
                        }
                    }
                }
            }

        }
        private void drawLines(Vector4[] points, List<int>[] poly)
        {
            Parallel.For(0, poly.Length, (i) =>
            {
                List<int> face = poly[i];
                bool isOutside = false;
                foreach (int point in face)
                {
                    if (points[point].W == 0)
                    {
                        isOutside = true;
                    }
                }
                if (!isOutside)
                {
                    for (int j = 0; j < face.Count; j++)
                    {
                        Vector4 v0 = points[face[j]];
                        Vector4 v1 = points[(face[((j + 1) % face.Count)])];
                        int x0 = (int)v0.X;
                        int y0 = (int)v0.Y;
                        int x1 = (int)v1.X;
                        int y1 = (int)v1.Y;
                        BresenhamLine(x0, y0, x1, y1, points);
                    }
                }

            });

        }
        private void onLeftMouseDown(object sender, MouseButtonEventArgs e)
        {
            Point p = e.GetPosition(this);
            yPrevMouse = (float)p.Y;
            xPrevMouse = (float)p.X;
            isPressed = true;
        }
        private void onLeftMouseUp(object sender, MouseButtonEventArgs e)
        {
            isPressed = false;
        }
        private void Button_Click(object sender, RoutedEventArgs e)
        {
            Microsoft.Win32.OpenFileDialog openFileDialog = new Microsoft.Win32.OpenFileDialog();
            openFileDialog.Filter = "Text Files (*.txt)|*.txt|All Files (*.*)|*.*";

            if (openFileDialog.ShowDialog() == true)
            {
                string filePath = openFileDialog.FileName;

                try
                {
                    string fileContent = File.ReadAllText(filePath);
                    String fileData = fileContent;
                    model = Parser.Parse(fileData);
                    MatrixTransformations.worldModel = new Vector4[model.GetPointCount()];
                    MatrixTransformations.projectionPoints = new Vector4[model.GetPointCount()];
                    // model.GetPoints().CopyTo(MatrixTransformations.worldModel,0);
                    render();
                }
                catch (IOException ex)
                {
                    MessageBox.Show($"Ошибка чтения файла: {ex.Message}");
                }
            }
        }

        private void PlusScaleClick(object sender, RoutedEventArgs e)
        {
            currentScale.X += 0.01f;
            currentScale.Y += 0.01f;
            currentScale.Z += 0.01f;
            render();
        }
        private void MinusScaleClick(object sender, RoutedEventArgs e)
        {
            currentScale.X -= 0.01f;
            currentScale.Y -= 0.01f;
            currentScale.Z -= 0.01f;
            render();
        }
        void Swap<T>(ref T x, ref T y)
        {
            T temp = x;
            x = y;
            y = temp;
        }

        private void Image_PreviewMouseWheel(object sender, MouseWheelEventArgs e)
        {
            int delta = e.Delta;
            currentScale.X += ((float)delta / 1000);
            currentScale.Y += ((float)delta / 1000);
            currentScale.Z += ((float)delta / 1000);
            render();
        }
        private void BresenhamLine(int x0, int y0, int x1, int y1, Vector4[] points)
        {
            var steep = Math.Abs(y1 - y0) > Math.Abs(x1 - x0); // Проверяем рост отрезка по оси икс и по оси игрек
                                                               // Отражаем линию по диагонали, если угол наклона слишком большой
            if (steep)
            {
                Swap(ref x0, ref y0); // Перетасовка координат вынесена в отдельную функцию для красоты
                Swap(ref x1, ref y1);
            }
            // Если линия растёт не слева направо, то меняем начало и конец отрезка местами
            if (x0 > x1)
            {
                Swap(ref x0, ref x1);
                Swap(ref y0, ref y1);
            }
            int dx = x1 - x0;
            int dy = Math.Abs(y1 - y0);
            int error = dx; // Здесь используется оптимизация с умножением на dx, чтобы избавиться от лишних дробей
            int ystep = (y0 < y1) ? 1 : -1; // Выбираем направление роста координаты y
            int y = y0;
            for (int x = x0; x <= x1; x++)
            {
                drawPoint(steep ? y : x, steep ? x : y, 255, 255, 255); // Не забываем вернуть координаты на место
                error -= dy * 2;
                if (error < 0)
                {
                    y += ystep;
                    error += dx * 2;
                }
            }
        }
        private void changeMode(object sender, RoutedEventArgs e)
        {
            wireFrameMode = !wireFrameMode;
            render();
        }


        private void i_MouseMove(object sender, MouseEventArgs e)
        {
            if (isPressed)
            {

                Point p = e.GetPosition(this);
                float xMouseDelta = (float)p.X - xPrevMouse;
                float yMouseDelta = (float)p.Y - yPrevMouse;
                xPrevMouse = (float)p.X;
                yPrevMouse = (float)p.Y;
                camera.rotateY(yMouseDelta, (float)drawingImage.Height);
                camera.rotateZ(xMouseDelta, (float)drawingImage.Width);
                xPrevMouse = (float)p.X;
                yPrevMouse = (float)p.Y;
                render();
            }
        }
    }
}
