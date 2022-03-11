package com.news.utils;

import java.util.List;
/*  TF_IDF ( term frequency–inverse document frequency )
    Viết tắt của thuật ngữ tiếng Anh term frequency – inverse document frequency,tf-idf là trọng số của một từ trong văn bản
    thu được qua thống kê thể hiện mức độ quan trọng của từ này trong một văn bản, mà bản thân văn bản đang xét nằm trong một
    tập hợp các văn bản.

    * Tf- term frequency : dùng để ước lượng tần xuất xuất hiện của từ trong văn bản. Tuy nhiên với mỗi văn bản thì có độ
    dài khác nhau, vì thế số lần xuất hiện của từ có thể nhiều hơn . Vì vậy số lần xuất hiện của từ sẽ được chia độ dài
    của văn bản (tổng số từ trong văn bản đó)
    ==> TF(t, d) = ( số lần từ t xuất hiện trong văn bản d) / (tổng số từ trong văn bản d)

    * DF- Inverse Document Frequency: dùng để ước lượng mức độ quan trọng của từ đó như thế nào . Khi tính tần số xuất hiện tf thì các từ đều được coi là quan trọng như nhau. Tuy nhiên có một số từ thường được được sử dụng nhiều nhưng không quan trọng để thể hiện ý nghĩa của đoạn văn , ví dụ :

    Từ nối: và, nhưng, tuy nhiên, vì thế, vì vậy, …
    Giới từ: ở, trong, trên, …
    Từ chỉ định: ấy, đó, nhỉ, …
    Vì vậy ta cần giảm đi mức độ quan trọng của những từ đó bằng cách sử dụng IDF :

    IDF(t, D) = log_e( Tổng số văn bản trong tập mẫu D/ Số văn bản có chứa từ t )
 */

public class TF_IDFUtil {
    public static double TF(List<String> doc, String term) {
        int result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word)) {
                result++;
            }
        }
        return (double)result / doc.size();
    }

    public static double IDF(List<List<String>> docs, String term) {
        int n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        if (docs.size() == n || n == 0) {
            return 1.0;
        } else {
            return (double)(1 + Math.log(docs.size() / n));
        }
    }

    public static double TF_IDF(List<String> doc, List<List<String>> docs, String term) {
        return TF(doc, term) * IDF(docs, term);
    }
}

