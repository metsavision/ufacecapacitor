package com.oezsoft;

public class oezfr {
	public native String getString();
	public native int getInteger();
	public native float[] SearchMatchArea(byte[] btFullImage, int nFullWidth, int nFullHeight, byte[] btSearchImage, int nSearchWidth, int nSearchHeight);
	public native int CheckEye(byte[] btImage, int nWidth, int nHeight);
	public native int CheckMovement(byte[] btImage1, byte[] btImage2, int nWidth, int nHeight);
	public native float GetBlurScore(byte[] btImage, int nWidth, int nHeight);
	public native int GetEdge(byte[] btImage, int nWidth, int nHeight, int nCanny1, int nCanny2, int nHough1, int nHough2);
	public native int CheckMovement2(byte[] btImage1, byte[] btImage2, int nStep);
	public native int InitFaceDetect(String strFace, String strEye, String strNose, String strMouth);
	public native int DetectFace(byte[] btImage);
	public native int GetFaceState();
	public native int GetEyeState();
	public native int GetNoseState();
	public native int GetMouthState();
	
	public native float[] GetEdge2(byte[] btImage, int nWidth, int nHeight, int nCanny, int nHough);
	public native void SetLogFile(String strLogFile);

	public native int GetIdType();

	public native String GetImageData();
	public native int GetDetectJudge();
	public native void SetRect(int nWidt, int nHeight);
}
