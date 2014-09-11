package ca.dealsaccess.example; 
 
import com.example.facejoy.R;

import android.app.Activity; 
import android.content.Context; 
import android.os.Bundle; 
import android.view.View; 
import android.view.ViewGroup; 
import android.widget.AdapterView; 
import android.widget.AdapterView.OnItemClickListener; 
import android.widget.BaseAdapter; 
import android.widget.GridView; 
import android.widget.ImageView; 
import android.widget.Toast; 
 
public class GridViewActivity extends Activity { 
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_gridview); 
        GridView gv = (GridView)findViewById(R.id.GridView1); 
        //为GridView设置适配器 
        gv.setAdapter(new MyAdapter(this)); 
        //注册监听事件 
        gv.setOnItemClickListener(new OnItemClickListener() 
        { 
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
            { 
                Toast.makeText(GridViewActivity.this, "pic" + position, Toast.LENGTH_SHORT).show(); 
            } 
        }); 
    } 
} 
    //自定义适配器 
    class MyAdapter extends BaseAdapter{ 
        //上下文对象 
        private Context context; 
        //图片数组 
        private Integer[] imgs = { 
               /* R.drawable.pic0, R.drawable.pic1, R.drawable.pic2,  
                R.drawable.pic3, R.drawable.pic4, R.drawable.pic5,                
                R.drawable.pic6, R.drawable.pic7, R.drawable.pic8,  
                R.drawable.pic0, R.drawable.pic1, R.drawable.pic2,  
                R.drawable.pic3, R.drawable.pic4, R.drawable.pic5,                
                R.drawable.pic6, R.drawable.pic7, R.drawable.pic8, */
        }; 
        MyAdapter(Context context){ 
            this.context = context; 
        } 
        public int getCount() { 
            return imgs.length; 
        } 
 
        public Object getItem(int item) { 
            return item; 
        } 
 
        public long getItemId(int id) { 
            return id; 
        } 
         
        //创建View方法 
        public View getView(int position, View convertView, ViewGroup parent) { 
             ImageView imageView; 
                if (convertView == null) { 
                    imageView = new ImageView(context); 
                    imageView.setLayoutParams(new GridView.LayoutParams(75, 75));//设置ImageView对象布局 
                    imageView.setAdjustViewBounds(false);//设置边界对齐 
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型 
                    imageView.setPadding(8, 8, 8, 8);//设置间距 
                }  
                else { 
                    imageView = (ImageView) convertView; 
                } 
                imageView.setImageResource(imgs[position]);//为ImageView设置图片资源 
                return imageView; 
        } 
} 