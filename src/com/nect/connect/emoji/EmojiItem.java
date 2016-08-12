package com.nect.connect.emoji;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.nect.connect.R;

public class EmojiItem {
	private static final SparseArray<String> sEmojisArray = new SparseArray<String>(55);
	
	static{
		sEmojisArray.put(1,  "!\\emoji_a1");
		sEmojisArray.put(2,  "!\\emoji_a2");
		sEmojisArray.put(3,  "!\\emoji_a3");
		sEmojisArray.put(4,  "!\\emoji_a4");
		sEmojisArray.put(5,  "!\\emoji_a5");
		sEmojisArray.put(6,  "!\\emoji_a6");
		sEmojisArray.put(7,  "!\\emoji_a7");
		sEmojisArray.put(8,  "!\\emoji_a8");
		sEmojisArray.put(9,  "!\\emoji_a9");
		sEmojisArray.put(10, "!\\emoji_a10");
		sEmojisArray.put(11, "!\\emoji_a11");
		sEmojisArray.put(12, "!\\emoji_a12");
		sEmojisArray.put(13, "!\\emoji_a13");
		sEmojisArray.put(14, "!\\emoji_a14");
		sEmojisArray.put(15, "!\\emoji_a15");
		sEmojisArray.put(16, "!\\emoji_a16");
		sEmojisArray.put(17, "!\\emoji_a17");
		sEmojisArray.put(18, "!\\emoji_a18");
		sEmojisArray.put(19, "!\\emoji_a19");
		sEmojisArray.put(20, "!\\emoji_a20");
		sEmojisArray.put(21, "!\\emoji_a21");
		sEmojisArray.put(22, "!\\emoji_a22");
		sEmojisArray.put(23, "!\\emoji_a23");
		sEmojisArray.put(24, "!\\emoji_a24");
		sEmojisArray.put(25, "!\\emoji_a25");
		sEmojisArray.put(26, "!\\emoji_a26");
		sEmojisArray.put(27, "!\\emoji_a27");
		sEmojisArray.put(28, "!\\emoji_a28");
		sEmojisArray.put(29, "!\\emoji_a29");
		sEmojisArray.put(30, "!\\emoji_a30");
		sEmojisArray.put(31, "!\\emoji_a31");
		sEmojisArray.put(32, "!\\emoji_a32");
		sEmojisArray.put(33, "!\\emoji_a33");
		sEmojisArray.put(34, "!\\emoji_a34");
		sEmojisArray.put(35, "!\\emoji_a35");
		sEmojisArray.put(36, "!\\emoji_a36");
		sEmojisArray.put(37, "!\\emoji_a37");
		sEmojisArray.put(38, "!\\emoji_a38");
		sEmojisArray.put(39, "!\\emoji_a39");
		sEmojisArray.put(40, "!\\emoji_a40");
		sEmojisArray.put(41, "!\\emoji_a41");
		sEmojisArray.put(42, "!\\emoji_a42");
		sEmojisArray.put(43, "!\\emoji_a43");
		sEmojisArray.put(44, "!\\emoji_a44");
		sEmojisArray.put(45, "!\\emoji_a45");
		sEmojisArray.put(46, "!\\emoji_a46");
		sEmojisArray.put(47, "!\\emoji_a47");
		sEmojisArray.put(48, "!\\emoji_a48");
		sEmojisArray.put(49, "!\\emoji_a49");
		sEmojisArray.put(50, "!\\emoji_a50");
		sEmojisArray.put(51, "!\\emoji_a51");
		sEmojisArray.put(52, "!\\emoji_a52");
		sEmojisArray.put(53, "!\\emoji_a53");
		sEmojisArray.put(54, "!\\emoji_a54");
		sEmojisArray.put(55, "!\\emoji_a55");
		
	}
	public static Integer[] mEmojiId={
		R.drawable.emoji_a1,
		R.drawable.emoji_a2,
		R.drawable.emoji_a3,
		R.drawable.emoji_a4,
		R.drawable.emoji_a5,
		R.drawable.emoji_a6,
		R.drawable.emoji_a7,
		R.drawable.emoji_a8,
		R.drawable.emoji_a9,
		R.drawable.emoji_a10,
		R.drawable.emoji_a11,
		R.drawable.emoji_a12,
		R.drawable.emoji_a13,
		R.drawable.emoji_a14,
		R.drawable.emoji_a15,
		R.drawable.emoji_a16,
		R.drawable.emoji_a17,
		R.drawable.emoji_a18,
		R.drawable.emoji_a19,
		R.drawable.emoji_a20,
		R.drawable.emoji_a21,
		R.drawable.emoji_a22,
		R.drawable.emoji_a23,
		R.drawable.emoji_a24,
		R.drawable.emoji_a25,
		R.drawable.emoji_a26,
		R.drawable.emoji_a27,
		R.drawable.emoji_a28,
		R.drawable.emoji_a29,
		R.drawable.emoji_a30,
		R.drawable.emoji_a31,
		R.drawable.emoji_a32,
		R.drawable.emoji_a33,
		R.drawable.emoji_a34,
		R.drawable.emoji_a35,
		R.drawable.emoji_a36,
		R.drawable.emoji_a37,
		R.drawable.emoji_a38,
		R.drawable.emoji_a39,
		R.drawable.emoji_a40,
		R.drawable.emoji_a41,
		R.drawable.emoji_a42,
		R.drawable.emoji_a43,
		R.drawable.emoji_a44,
		R.drawable.emoji_a45,
		R.drawable.emoji_a46,
		R.drawable.emoji_a47,
		R.drawable.emoji_a48,
		R.drawable.emoji_a49,
		R.drawable.emoji_a50,
		R.drawable.emoji_a51,
		R.drawable.emoji_a52,
		R.drawable.emoji_a53,
		R.drawable.emoji_a54,
		R.drawable.emoji_a55
		
	};
	
	public static Integer[] getEmpjiIds()
	{
		return mEmojiId;
		
	}

	public static String getEmojiChar(int position)
	{
		
		return sEmojisArray.get(position);
		
	}
	
	
}
