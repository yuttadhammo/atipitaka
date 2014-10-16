package org.yuttadhammo.tipitaka;

import java.util.HashMap;

public class PaliUtils {
	public static String toUni(String string) {
		string = string.replace("aa", "ā").replace("ii", "ī").replace("uu", "ū").replace(".t", "ṭ").replace(".d", "ḍ").replace("\"n", "ṅ").replace(".n", "ṇ").replace(".m", "ṃ").replace("~n", "ñ").replace(".l", "ḷ");
		return string;
	}

	public static String toVel(String string) {
		string = string.replaceAll("ā", "aa").replaceAll("ī", "ii").replaceAll("ū", "uu").replaceAll("ṭ", ".t").replaceAll("ḍ", ".d").replaceAll("ṅ", "\"n").replaceAll("ṇ", ".n").replaceAll("[ṃṁ]", ".m").replaceAll("ñ", "~n").replaceAll("ḷ", ".l").replaceAll("Ā", "AA").replaceAll("Ī", "II").replaceAll("Ū", "UU").replaceAll("Ṭ", ".T").replaceAll("Ḍ", ".D").replaceAll("Ṅ", "\"N").replaceAll("Ṇ", ".N").replaceAll("[ṂṀ]",".M").replaceAll("Ñ", "~N").replaceAll("Ḷ", ".L");
		return string;
	}
	public static String colorToHexString(int color) {
		return String.format("#%06X", 0xFFFFFFFF & color);
	}

    public String toFuzzy(String input){
        if(input == null) 
            return input;
        //input = toVel(input).replace(/\.([tdnlmTDNLM])/g,"$1").replace(/~([nN])/g,"$1").replace(/"([nN])/g,"$1").replace("aa","a").replace("ii","i").replace("uu","u").replace("nn","n").replace("mm","m").replace("yy","y").replace("ll","l").replace("ss","s").replace(/([kgcjtdpb])[kgcjtdpb]{0,1}h*/g,"$1");
        return input;
    }

    public String toSkt(String input,boolean rv) {
        if(input == null || input.equals("")) return input;

        if(rv) {
            input = input.replace("A","aa").replace("I","ii").replace("U","uu").replace("f",".r").replace("F",".rr").replace("x",".l").replace("X",".ll").replace("E","ai").replace("O","au").replace("K","kh").replace("G","gh").replace("N","n").replace("C","ch").replace("J","jh").replace("Y","~n").replace("w",".t").replace("q",".d").replace("W",".th").replace("Q",".dh").replace("R",".n").replace("T","th").replace("D","dh").replace("P","ph").replace("B","bh").replace("S","s").replace("z",".s").replace("M",".m").replace("H",".h");
        }
        else {
            input = input.replace("aa","A").replace("ii","I").replace("uu","U").replace(".r","f").replace(".rr","F").replace(".l","x").replace(".ll","X").replace("ai","E").replace("au","O").replace("kh","K").replace("gh","G").replace("n","N").replace("ch","C").replace("jh","J").replace("~n","Y").replace(".t","w").replace(".d","q").replace(".th","W").replace(".dh","Q").replace(".n","R").replace("th","T").replace("dh","D").replace("ph","P").replace("bh","B").replace("s","S").replace(".s","z").replace(".m","M").replace(".h","H");
        }
        return input;
    }


    public static String toSin(String input) {
        input = input.toLowerCase().replace("ṁ","ṃ");
        HashMap<String,String> vowel = new HashMap<String, String>();

        vowel.put("a","අ");
        vowel.put("ā","ආ");
        vowel.put("i","ඉ");
        vowel.put("ī","ඊ");
        vowel.put("u","උ");
        vowel.put("ū","ඌ");
        vowel.put("e","එ");
        vowel.put("o","ඔ");

        HashMap<String,String> sinhala = new HashMap<String,String>();

        sinhala.put("ā","ා");
        sinhala.put("i","ි");
        sinhala.put("ī","ී");
        sinhala.put("u","ු");
        sinhala.put("ū","ූ");
        sinhala.put("e","ෙ");
        sinhala.put("o","ො");
        sinhala.put("ṃ","ං");
        sinhala.put("k","ක");
        sinhala.put("g","ග");
        sinhala.put("ṅ","ඞ");
        sinhala.put("c","ච");
        sinhala.put("j","ජ");
        sinhala.put("ñ","ඤ");
        sinhala.put("ṭ","ට");
        sinhala.put("ḍ","ඩ");
        sinhala.put("ṇ","ණ");
        sinhala.put("t","ත");
        sinhala.put("d","ද");
        sinhala.put("n","න");
        sinhala.put("p","ප");
        sinhala.put("b","බ");
        sinhala.put("m","ම");
        sinhala.put("y","ය");
        sinhala.put("r","ර");
        sinhala.put("l","ල");
        sinhala.put("ḷ","ළ");
        sinhala.put("v","ව");
        sinhala.put("s","ස");
        sinhala.put("h","හ");

        HashMap<String,String> conj = new HashMap<String,String>();

        conj.put("kh","ඛ");
        conj.put("gh","ඝ");
        conj.put("ch","ඡ");
        conj.put("jh","ඣ");
        conj.put("ṭh","ඨ");
        conj.put("ḍh","ඪ");
        conj.put("th","ථ");
        conj.put("dh","ධ");
        conj.put("ph","ඵ");
        conj.put("bh","භ");
        conj.put("jñ","ඥ");
        conj.put("ṇḍ","ඬ");
        conj.put("nd","ඳ");
        conj.put("mb","ඹ");
        conj.put("rg","ඟ");


        HashMap<String,String> cons = new HashMap<String,String>();

        cons.put("k","ක");
        cons.put("g","ග");
        cons.put("ṅ","ඞ");
        cons.put("c","ච");
        cons.put("j","ජ");
        cons.put("ñ","ඤ");
        cons.put("ṭ","ට");
        cons.put("ḍ","ඩ");
        cons.put("ṇ","ණ");
        cons.put("t","ත");
        cons.put("d","ද");
        cons.put("n","න");
        cons.put("p","ප");
        cons.put("b","බ");
        cons.put("m","ම");
        cons.put("y","ය");
        cons.put("r","ර");
        cons.put("l","ල");
        cons.put("ḷ","ළ");
        cons.put("v","ව");
        cons.put("s","ස");
        cons.put("h","හ");


        String im = "";
        String i0 = "";
        String i1 = "";
        String i2 = "";
        String i3 = "";
        String i4 = "";
        String i5 = "";
        String output = "";
        int i = 0;

        input = input.replace("&quot;", "`");

        while (i < input.length()) {

            im = i > 1 ? input.substring(i - 2, i-1) : "";
            i0 = i > 0 ? input.substring(i - 1, i) : "";
            i1 = input.substring(i,i+1);
            i2 = i < input.length() - 1 ? input.substring(i+1,i+2) : "";
            i3 = i < input.length() - 2 ? input.substring(i+2,i+3) : "";
            i4 = i < input.length() - 3 ? input.substring(i+3,i+4) : "";
            i5 = i < input.length() - 4 ? input.substring(i+4,i+5) : "";
    
            if(vowel.containsKey(i1)) {
                if (i == 0 || i0.equals("a")) output += vowel.get(i1);
                else if (!i1.equals("a")) {
                    output += sinhala.get(i1);
                }
                i++;
            }
            else if (conj.containsKey(i1 + i2)) {		// two character match
                output += conj.get(i1+i2);
                i += 2;
                if(cons.containsKey(i3)) output += "්";
            }
            else if (sinhala.containsKey(i1) && !i1.equals("a")) {		// one character match except a
                output += sinhala.get(i1);
                i++;
                if(cons.containsKey(i2) && !i1.equals("ṃ")) output += "්";
            }
            else if (!sinhala.containsKey(i1)) {
                if (cons.containsKey(i0) || (i0.equals("h") && cons.containsKey(im))) output += "්"; // end word consonant
                output += i1;
                i++;
                if (vowel.containsKey(i2)) {  // word-beginning vowel marker
                    output += vowel.get(i2);
                    i++;
                }
            }
            else i++;
        }
        if (cons.containsKey(i1)) output += "්";

        // fudges

        // "‍" zero-width joiner inside of quotes

        output = output.replace("ඤ්ජ", "ඦ");
        output = output.replace("ණ්ඩ", "ඬ");
        output = output.replace("න්ද", "ඳ");
        output = output.replace("ම්බ", "ඹ");
        output = output.replace("්ර", "්‍ර");
        output = output.replaceAll("`+", "");
        return output;
    }


/*
    public String fromSin(input,type) {
        HashMap<String,String> vowel = new HashMap<String,String>();

        vowel.put("අ","a");
        vowel.put("ආ","ā");
        vowel.put("ඉ","i");
        vowel.put("ඊ","ī");
        vowel.put("උ","u");
        vowel.put("ඌ","ū");
        vowel.put("එ","e");
        vowel.put("ඔ","o");


        vowel.put("ඒ","ē");
        vowel.put("ඇ","ai");
        vowel.put("ඈ","āi");
        vowel.put("ඕ","ō");
        vowel.put("ඖ","au");

        vowel.put("ා","ā");
        vowel.put("ි","i");
        vowel.put("ී","ī");
        vowel.put("ු","u");
        vowel.put("ූ","ū");
        vowel.put("ෙ","e");
        vowel.put("ො","o");

        vowel.put("ෘ","ṛ");
        vowel.put("ෟ","ḷ");
        vowel.put("ෲ","ṝ");
        vowel.put("ෳ","ḹ");

        vowel.put("ේ","ē");
        vowel.put("ැ","ae");
        vowel.put("ෑ","āe");
        vowel.put("ෛ","ai");
        vowel.put("ෝ","ō");
        vowel.put("ෞ","au");

        HashMap<String,String> sinhala = new HashMap<String,String>();


        sinhala.put("ං","ṃ");
        sinhala.put("ක","k");
        sinhala.put("ඛ","kh");
        sinhala.put("ග","g");
        sinhala.put("ඝ","gh");
        sinhala.put("ඞ","ṅ");
        sinhala.put("ච","c");
        sinhala.put("ඡ","ch");
        sinhala.put("ජ","j");
        sinhala.put("ඣ","jh");
        sinhala.put("ඤ","ñ");
        sinhala.put("ට","ṭ");
        sinhala.put("ඨ","ṭh");
        sinhala.put("ඩ","ḍ");
        sinhala.put("ඪ","ḍh");
        sinhala.put("ණ","ṇ");
        sinhala.put("ත","t");
        sinhala.put("ථ","th");
        sinhala.put("ද","d");
        sinhala.put("ධ","dh");
        sinhala.put("න","n");
        sinhala.put("ප","p");
        sinhala.put("ඵ","ph");
        sinhala.put("බ","b");
        sinhala.put("භ","bh");
        sinhala.put("ම","m");
        sinhala.put("ය","y");
        sinhala.put("ර","r");

        sinhala.put("ල","l");
        sinhala.put("ළ","ḷ");
        sinhala.put("ව","v");
        sinhala.put("ස","s");
        sinhala.put("හ","h");

        sinhala.put("ෂ","ṣ");
        sinhala.put("ශ","ś");

        sinhala.put("ඥ","jñ");
        sinhala.put("ඬ","ṇḍ");
        sinhala.put("ඳ","nd");
        sinhala.put("ඹ","mb");
        sinhala.put("ඟ","rg");

        var im, i0, i1, i2, i3
        var output = "";
        var i = 0;

        input = input.replace("\&quot;", "`");

        while (i < input.length) {
            i1 = input.substring(i);

            if (vowel.get(i1)) {
                if(output.substring(output.length-1) == "a")
                    output = output.substring(0,output.length-1);
                output += vowel.get(i1);
            }
            else if (sinhala.get(i1)) {
                output += sinhala.get(i1)+"a";
            }
            else
                output += i1;
            i++;
        }

        // fudges

        // "‍" zero-width joiner inside of quotes

        output = output.replace("a්", "");
        return output;
    }
*/

    public static String toMyanmar(String input) {
        input = input.toLowerCase().replace("ṁ","ṃ");
        HashMap<String,String> vowel = new HashMap<String,String>();
        vowel.put("a","အ");
        vowel.put("i","ဣ");
        vowel.put("u","ဥ");
        vowel.put("ā","အာ");
        vowel.put("ī","ဤ");
        vowel.put("ū","ဦ");
        vowel.put("e","ဧ");
        vowel.put("o","ဩ");

        HashMap<String,String> myanr = new HashMap<String,String>();

//	myanr.put("ā","ā"); // later
        myanr.put("i","ိ");
        myanr.put("ī","ီ");
        myanr.put("u","ု");
        myanr.put("ū","ူ");
        myanr.put("e","ေ");
//	myanr.put("o","ေā"); // later
        myanr.put("ṃ","ံ");
        myanr.put("k","က");
        myanr.put("kh","ခ");
        myanr.put("g","ဂ");
        myanr.put("gh","ဃ");
        myanr.put("ṅ","င");
        myanr.put("c","စ");
        myanr.put("ch","ဆ");
        myanr.put("j","ဇ");
        myanr.put("jh","ဈ");
        myanr.put("ñ","ဉ");
        myanr.put("ṭ","ဋ");
        myanr.put("ṭh","ဌ");
        myanr.put("ḍ","ဍ");
        myanr.put("ḍh","ဎ");
        myanr.put("ṇ","ဏ");
        myanr.put("t","တ");
        myanr.put("th","ထ");
        myanr.put("d","ဒ");
        myanr.put("dh","ဓ");
        myanr.put("n","န");
        myanr.put("p","ပ");
        myanr.put("ph","ဖ");
        myanr.put("b","ဗ");
        myanr.put("bh","ဘ");
        myanr.put("m","မ");
        myanr.put("y","ယ");
        myanr.put("r","ရ");
        myanr.put("l","လ");
        myanr.put("ḷ","ဠ");
        myanr.put("v","ဝ");
        myanr.put("s","သ");
        myanr.put("h","ဟ");

        HashMap<String,String> cons = new HashMap<String,String>();

        cons.put("k","က");
        cons.put("g","ဂ");
        cons.put("ṅ","င");
        cons.put("c","စ");
        cons.put("j","ဇ");
        cons.put("ñ","ဉ");
        cons.put("ṭ","ဋ");
        cons.put("ḍ","ဍ");
        cons.put("ṇ","ဏ");
        cons.put("t","တ");
        cons.put("d","ဒ");
        cons.put("n","န");
        cons.put("p","ပ");
        cons.put("b","ဗ");
        cons.put("m","မ");
        cons.put("y","ယ");
        cons.put("r","ရ");
        cons.put("l","လ");
        cons.put("ḷ","ဠ");
        cons.put("v","ဝ");
        cons.put("s","သ");
        cons.put("h","ဟ");


        HashMap<String,Integer> spec = new HashMap<String,Integer>(); // takes special aa
        spec.put("kh",1);
        spec.put("g",1);
        spec.put("d",1);
        spec.put("dh",1);
        spec.put("p",1);
        spec.put("v",1);

        String im = "";
        String i0 = "";
        String i1 = "";
        String i2 = "";
        String i3 = "";
        String i4 = "";
        String i5 = "";
        String output = "";
        int i = 0;

        input = input.replace("&quot;", "`");

        String longa = null; // special character for long a

        while (i < input.length()) {

            im = i > 1 ? input.substring(i - 2, i-1) : "";
            i0 = i > 0 ? input.substring(i - 1, i) : "";
            i1 = input.substring(i,i+1);
            i2 = i < input.length() - 1 ? input.substring(i+1,i+2) : "";
            i3 = i < input.length() - 2 ? input.substring(i+2,i+3) : "";
            i4 = i < input.length() - 3 ? input.substring(i+3,i+4) : "";
            i5 = i < input.length() - 4 ? input.substring(i+4,i+5) : "";

            if (vowel.containsKey(i1)) {
                if (i == 0 || i0.equals("a")) output += vowel.get(i1);
                else if (i1.equals("ā")) {
                    if (spec.containsKey(longa))
                        output += "ါ";
                    else
                        output += "ာ";
                }
                else if (i1.equals("o")) {
                    if (spec.containsKey(longa))
                        output += "ေါ";
                    else
                        output += "ော";
                }
                else if (!i1.equals("a")) {
                    output += myanr.get(i1);
                }
                i++;
                longa = null;
            }
            else if (myanr.containsKey(i1 + i2) && i2.equals("h")) {	// two character match
                output += myanr.get(i1+i2);
                if (!i3.equals("y") && longa == null) longa = i1+i2; // gets first letter in conjunct for special long a check
                if(cons.containsKey(i3)) output += "္";
                i += 2;
            }
            else if (myanr.containsKey(i1) && !i1.equals("a")) {	// one character match except a
                output += myanr.get(i1);
                i++;
                if (!i2.equals("y") && !i2.equals("") && longa == null) longa = i1; // gets first letter in conjunct for special long a check
                if(cons.containsKey(i2) && !i1.equals("ṃ")) {
                    output += "္";
                }
            }
            else if (!myanr.containsKey(i1)) {
                output += i1;
                i++;
                if (vowel.containsKey(i2)) {  // word-beginning vowel marker
                    if (vowel.containsKey(i2 + i3)) {
                        output += vowel.get(i2+i3);
                        i += 2;
                    }
                    else {
                        output += vowel.get(i2);
                        i++;
                    }
                }
                longa = null;
            }
            else {
                longa = null;
                i++;
            }
        }

        // fudges

        output = output.replace("ဉ္ဉ", "ည");
        output = output.replace("္ယ", "ျ");
        output = output.replace("္ရ", "ြ");
        output = output.replace("္ဝ", "ွ");
        output = output.replace("္ဟ", "ှ");
        output = output.replace("သ္သ", "ဿ");
        output = output.replace("င္", "င်္");

        output = output.replace("`+", "\"");
        return output;
    }


    public static String toDeva(String input) {

        input = input.toLowerCase().replace("ṁ","ṃ");

        HashMap<String,String> vowel = new HashMap<String,String>();
        vowel.put("a"," अ");
        vowel.put("i"," इ");
        vowel.put("u"," उ");
        vowel.put("ā"," आ");
        vowel.put("ī"," ई");
        vowel.put("ū"," ऊ");
        vowel.put("e"," ए");
        vowel.put("o"," ओ");

        HashMap<String,String> devar = new HashMap<String,String>();

        devar.put("ā","ा");
        devar.put("i","ि");
        devar.put("ī","ी");
        devar.put("u","ु");
        devar.put("ū","ू");
        devar.put("e","े");
        devar.put("o","ो");
        devar.put("ṃ","ं");
        devar.put("k","क");
        devar.put("kh","ख");
        devar.put("g","ग");
        devar.put("gh","घ");
        devar.put("ṅ","ङ");
        devar.put("c","च");
        devar.put("ch","छ");
        devar.put("j","ज");
        devar.put("jh","झ");
        devar.put("ñ","ञ");
        devar.put("ṭ","ट");
        devar.put("ṭh","ठ");
        devar.put("ḍ","ड");
        devar.put("ḍh","ढ");
        devar.put("ṇ","ण");
        devar.put("t","त");
        devar.put("th","थ");
        devar.put("d","द");
        devar.put("dh","ध");
        devar.put("n","न");
        devar.put("p","प");
        devar.put("ph","फ");
        devar.put("b","ब");
        devar.put("bh","भ");
        devar.put("m","म");
        devar.put("y","य");
        devar.put("r","र");
        devar.put("l","ल");
        devar.put("ḷ","ळ");
        devar.put("v","व");
        devar.put("s","स");
        devar.put("h","ह");


        HashMap<String,String> cons = new HashMap<String,String>();

        cons.put("k","क");
        cons.put("kh","ख");
        cons.put("g","ग");
        cons.put("gh","घ");
        cons.put("ṅ","ङ");
        cons.put("c","च");
        cons.put("ch","छ");
        cons.put("j","ज");
        cons.put("jh","झ");
        cons.put("ñ","ञ");
        cons.put("ṭ","ट");
        cons.put("ṭh","ठ");
        cons.put("ḍ","ड");
        cons.put("ḍh","ढ");
        cons.put("ṇ","ण");
        cons.put("t","त");
        cons.put("th","थ");
        cons.put("d","द");
        cons.put("dh","ध");
        cons.put("n","न");
        cons.put("p","प");
        cons.put("ph","फ");
        cons.put("b","ब");
        cons.put("bh","भ");
        cons.put("m","म");
        cons.put("y","य");
        cons.put("r","र");
        cons.put("l","ल");
        cons.put("ḷ","ळ");
        cons.put("v","व");
        cons.put("s","स");
        cons.put("h","ह");

        String im = "";
        String i0 = "";
        String i1 = "";
        String i2 = "";
        String i3 = "";
        String i4 = "";
        String i5 = "";
        String output = "";
        int i = 0;

        input = input.replace("&quot;", "`");

        while (i < input.length()) {

            im = i > 1 ? input.substring(i - 2, i-1) : "";
            i0 = i > 0 ? input.substring(i - 1, i) : "";
            i1 = input.substring(i,i+1);
            i2 = i < input.length() - 1 ? input.substring(i+1,i+2) : "";
            i3 = i < input.length() - 2 ? input.substring(i+2,i+3) : "";
            i4 = i < input.length() - 3 ? input.substring(i+3,i+4) : "";
            i5 = i < input.length() - 4 ? input.substring(i+4,i+5) : "";

            if (i == 0 && vowel.containsKey(i1)) { // first letter vowel
                output += vowel.get(i1);
                i += 1;
            }
            else if (i2.equals("h") && devar.containsKey(i1 + i2)) {		// two character match
                output += devar.get(i1+i2);
                if (!i3.equals("") && !vowel.containsKey(i3) && !i2.equals("ṃ") && (!i1.equals("ṅ") || (!i2.equals("g") && !i2.equals("k")))) {
                    output += "्";
                }
                i += 2;
            }
            else if (devar.containsKey(i1)) {	// one character match except a
                output += devar.get(i1);
                if (!i2.equals("") && !vowel.containsKey(i2) && !vowel.containsKey(i1) && !i1.equals("ṃ") && (!i1.equals("ṅ") || (!i2.equals("g") && !i2.equals("k")))) {
                    output += "्";
                }
                i++;
            }
            else if(!i1.equals("a")) { // non-Pali
                output += i1;
                if(vowel.containsKey(i2)) {
                    output += vowel.get(i2);
                    i++;
                }
                i++;
            }
            else i++; // a
        }
        if (cons.containsKey(i1)) output += "्";
        output = output.replace("`+", "\"");
        return output;
    }

    public static String toThai(String input) {
        input = input.toLowerCase().replace("ṁ","ṃ");

        HashMap<String,String> vowel = new HashMap<String,String>();
        vowel.put("a","1");
        vowel.put("ā","1");
        vowel.put("i","1");
        vowel.put("ī","1");
        vowel.put("iṃ","1");
        vowel.put("u","1");
        vowel.put("ū","1");
        vowel.put("e","2");
        vowel.put("o","2");


        HashMap<String,String> thair = new HashMap<String,String>();
        thair.put("a","อ");
        thair.put("ā","า");
        thair.put("i","ิ");
        thair.put("ī","ี");
        thair.put("iṃ","ึ");
        thair.put("u","ุ");
        thair.put("ū","ู");
        thair.put("e","เ");
        thair.put("o","โ");
        thair.put("ṃ","ํ");
        thair.put("k","ก");
        thair.put("kh","ข");
        thair.put("g","ค");
        thair.put("gh","ฆ");
        thair.put("ṅ","ง");
        thair.put("c","จ");
        thair.put("ch","ฉ");
        thair.put("j","ช");
        thair.put("jh","ฌ");
        thair.put("ñ","ญ");//"");
        thair.put("ṭ","ฏ");
        thair.put("ṭh","ฐ");//"");
        thair.put("ḍ","ฑ");
        thair.put("ḍh","ฒ");
        thair.put("ṇ","ณ");
        thair.put("t","ต");
        thair.put("th","ถ");
        thair.put("d","ท");
        thair.put("dh","ธ");
        thair.put("n","น");
        thair.put("p","ป");
        thair.put("ph","ผ");
        thair.put("b","พ");
        thair.put("bh","ภ");
        thair.put("m","ม");
        thair.put("y","ย");
        thair.put("r","ร");
        thair.put("l","ล");
        thair.put("ḷ","ฬ");
        thair.put("v","ว");
        thair.put("s","ส");
        thair.put("h","ห");

        HashMap<String,String> cons = new HashMap<String,String>();

        cons.put("k","1");
        cons.put("g","1");
        cons.put("ṅ","1");
        cons.put("c","1");
        cons.put("j","1");
        cons.put("ñ","1");
        cons.put("ṭ","1");
        cons.put("ḍ","1");
        cons.put("ṇ","1");
        cons.put("t","1");
        cons.put("d","1");
        cons.put("n","1");
        cons.put("p","1");
        cons.put("b","1");
        cons.put("m","1");
        cons.put("y","1");
        cons.put("r","1");
        cons.put("l","1");
        cons.put("ḷ","1");
        cons.put("v","1");
        cons.put("s","1");
        cons.put("h","1");

        String im = "";
        String i0 = "";
        String i1 = "";
        String i2 = "";
        String i3 = "";
        String i4 = "";
        String i5 = "";
        String output = "";
        int i = 0;

        input = input.replace("&quot;", "`");

        while (i < input.length()) {

            im = i > 1 ? input.substring(i - 2, i-1) : "";
            i0 = i > 0 ? input.substring(i - 1, i) : "";
            i1 = input.substring(i,i+1);
            i2 = i < input.length() - 1 ? input.substring(i+1,i+2) : "";
            i3 = i < input.length() - 2 ? input.substring(i+2,i+3) : "";
            i4 = i < input.length() - 3 ? input.substring(i+3,i+4) : "";
            i5 = i < input.length() - 4 ? input.substring(i+4,i+5) : "";

            if (vowel.containsKey(i1)) {
                if (i1.equals("o") || i1.equals("e")) {
                    output += thair.get(i1) + thair.get("a");
                    i++;
                }
                else {
                    if (i == 0) {
                        output += thair.get("a");
                    }
                    if (i1.equals("i") && i2.equals("ṃ")) { // special i.m character
                        output += thair.get(i1+i2);
                        i++;
                    }
                    else if (!i1.equals("a")) { output += thair.get(i1); }
                    i++;
                }
            }
            else if (thair.containsKey(i1 + i2) && i2.equals("h")) {		// two character match
                if (i3.equals("o") || i3.equals("e")) {
                    output += thair.get(i3);
                    i++;
                }
                output += thair.get(i1+i2);
                if (cons.containsKey(i3)) output += "ฺ";
                i = i + 2;
            }
            else if (thair.containsKey(i1) && !i1.equals("a")) {		// one character match except a
                if (i2.equals("o") || i2.equals("e")) {
                    output += thair.get(i2);
                    i++;
                }
                output += thair.get(i1);
                if (cons.containsKey(i2) && !i1.equals("ṃ")) output += "ฺ";
                i++;
            }
            else if (!thair.containsKey(i1)) {
                output += i1;
                if (cons.containsKey(i0) || (i0.equals("h") && cons.containsKey(im))) output += "ฺ";
                i++;
                if (i2.equals("o") || i2.equals("e")) {  // long vowel first
                    output += thair.get(i2);
                    i++;
                }
                if (vowel.containsKey(i2)) {  // word-beginning vowel marker
                    output += thair.get("a");
                }
            }
            else { // a
                i++;
            }
        }
        if (cons.containsKey(i1)) output += "ฺ";
        output = output.replace("`+", "\"");
        return output;
    }
/*
    public String fromThai(input) {

        output = input.replace(/([อกขคฆงจฉชฌญฏฐฑฒณตถทธนปผพภมยรลฬวสห])(?!ฺ)/g, "$1a").replace(/([เโ])([อกขคฆงจฉชฌญฏฐฑฒณตถทธนปผพภมยรลฬวสหฺฺ]+a)/g, "$2$1").replace(/[a]([าิีึุูเโ])/g, "$1").replace("ฺ", "");

        output = output.replace("อ","").replace("า","ā").replace("ิ","i").replace("ี","ī").replace("ึ","iṃ").replace("ุ","u").replace("ู","ū").replace("เ","e").replace("โ","o").replace("ํ","ṃ").replace("ก","k").replace("ข","kh").replace("ค","g").replace("ฆ","gh").replace("ง","ṅ").replace("จ","c").replace("ฉ","ch").replace("ช","j").replace("ฌ","jh").replace("","ñ").replace("ญ","ñ").replace("ฏ","ṭ").replace("","ṭh").replace("ฐ","ṭh").replace("ฑ","ḍ").replace("ฒ","ḍh").replace("ณ","ṇ").replace("ต","t").replace("ถ","th").replace("ท","d").replace("ธ","dh").replace("น","n").replace("ป","p").replace("ผ","ph").replace("พ","b").replace("ภ","bh").replace("ม","m").replace("ย","y").replace("ร","r").replace("ล","l").replace("ฬ","ḷ").replace("ว","v").replace("ส","s").replace("ห","h").replace("๐","0").replace("๑","1").replace("๒","2").replace("๓","3").replace("๔","4").replace("๕","5").replace("๖","6").replace("๗","7").replace("๘","8").replace("๙","9").replace("ฯ","...");

        output = output.replace("","");

        return output;
    }
*/
    public static String translit(String data, int script) {
        if(data == null || data.equals(""))
            return data;

        data = data.replace("&nbsp;"," ");

        String out = "";
        switch (script) {
            case 0:
                out = data;
                break;
            case 1:
                out = toThai(data);
                break;
            case 2:
                out = toDeva(data);
                break;
            case 3:
                out = toMyanmar(data);
                break;
            case 4:
                out = toSin(data);
                break;
        }
        return out;
    }

}
