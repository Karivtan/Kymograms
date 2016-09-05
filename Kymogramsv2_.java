package HomeMade.Tools;
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import ij.measure.ResultsTable;
import java.util.*;

public class Kymogramsv2_ implements PlugIn {
	double[] yV = new double[0];
	double[][]Kymogram; 
	ImageProcessor Kymo;
	ImagePlus Kymograms;
	ResultsTable rt = ResultsTable.getResultsTable();
	int LineWidth;
	
	public void run(String arg) {
		ImagePlus imp = IJ.getImage();
		rt.reset();
		Roi cRoi =imp.getRoi();
		if (cRoi==null){
			IJ.setTool("line");
			new WaitForUserDialog("Draw the line you want to analyze").show();
		} else if (!cRoi.isLine()){
			IJ.showMessage("Line profile needed");
		} else {
			Kymograms=getKymogram(imp, cRoi);
		}
		Kymograms.show();
	} 

	public ImagePlus getKymogram(ImagePlus imp, Roi cRoi){
		int nSlices = Math.max(imp.getNSlices(),imp.getNFrames());
		
		Kymogram = new double[nSlices][yV.length];
		imp.setSlice(1);
		for (int i=0;i<nSlices;i++){
			imp.setSlice(i+1);
			ProfilePlot pp =new ProfilePlot(imp, true);
			yV=pp.getProfile();
			Kymogram[i]=yV;
		}
		for (int i=0;i<nSlices;i++){
			for (int j=0;j<yV.length;j++){
				rt.setValue(i,j,Kymogram[i][j]);
			}
		}
		Kymo = rt.getTableAsImage();
		Kymograms=new ImagePlus("Kymogram",Kymo);
		return Kymograms;
		
	}
}
