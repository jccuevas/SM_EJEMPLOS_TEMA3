package es.uja.ejemplostema3;


import java.net.MalformedURLException;
import java.net.URL;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NetworkURLFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		FragmentManager fm=getFragmentManager();
		
//		if(fm.findFragmentById(R.id.layout_fragment_network_URL)!=null)
//			setHasOptionsMenu(true);//Para comunicar que el panel quiere recibir los eventos de la barra de acci�n
//		
		return inflater.inflate(R.layout.layout_network_url, container);
		
	}
	
	public URL getURI()
	{
		EditText urle = (EditText)getActivity().findViewById(R.id.network_url_editText_URL);
		
		try {
			URL url = new URL(urle.getEditableText().toString());
			
			//Toast.makeText(getActivity(),"URL host="+url.getHost()+" Puerto="+url.getPort(), Toast.LENGTH_LONG).show();
			return url;
		} catch (MalformedURLException e) {
			//Toast.makeText(getActivity(),"Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
			return null;
		}
		
	}
	
	public String getURLString()
	{
		EditText urle = (EditText)getActivity().findViewById(R.id.network_url_editText_URL);
		return urle.getEditableText().toString();
			
		
		
	}

}
