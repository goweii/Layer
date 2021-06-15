package per.goweii.layer.keyboard;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class KeyboardUtils {
    public static Map<Integer, Integer> KEYCODE_MAP = new HashMap<>();

    static {
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_a, R.string.layer_keyboard_keyname_a);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_b, R.string.layer_keyboard_keyname_b);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_c, R.string.layer_keyboard_keyname_c);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_d, R.string.layer_keyboard_keyname_d);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_e, R.string.layer_keyboard_keyname_e);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_f, R.string.layer_keyboard_keyname_f);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_g, R.string.layer_keyboard_keyname_g);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_h, R.string.layer_keyboard_keyname_h);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_i, R.string.layer_keyboard_keyname_i);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_j, R.string.layer_keyboard_keyname_j);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_k, R.string.layer_keyboard_keyname_k);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_l, R.string.layer_keyboard_keyname_l);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_m, R.string.layer_keyboard_keyname_m);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_n, R.string.layer_keyboard_keyname_n);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_o, R.string.layer_keyboard_keyname_o);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_p, R.string.layer_keyboard_keyname_p);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_q, R.string.layer_keyboard_keyname_q);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_r, R.string.layer_keyboard_keyname_r);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_s, R.string.layer_keyboard_keyname_s);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_t, R.string.layer_keyboard_keyname_t);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_u, R.string.layer_keyboard_keyname_u);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_v, R.string.layer_keyboard_keyname_v);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_w, R.string.layer_keyboard_keyname_w);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_x, R.string.layer_keyboard_keyname_x);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_y, R.string.layer_keyboard_keyname_y);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_z, R.string.layer_keyboard_keyname_z);

        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_0, R.string.layer_keyboard_keyname_0);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_1, R.string.layer_keyboard_keyname_1);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_2, R.string.layer_keyboard_keyname_2);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_3, R.string.layer_keyboard_keyname_3);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_4, R.string.layer_keyboard_keyname_4);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_5, R.string.layer_keyboard_keyname_5);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_6, R.string.layer_keyboard_keyname_6);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_7, R.string.layer_keyboard_keyname_7);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_8, R.string.layer_keyboard_keyname_8);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_9, R.string.layer_keyboard_keyname_9);

        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_dot, R.string.layer_keyboard_keyname_dot);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_comma, R.string.layer_keyboard_keyname_comma);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_add, R.string.layer_keyboard_keyname_add);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_subtract, R.string.layer_keyboard_keyname_subtract);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_star, R.string.layer_keyboard_keyname_star);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_slash, R.string.layer_keyboard_keyname_slash);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_exclamation, R.string.layer_keyboard_keyname_exclamation);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_question, R.string.layer_keyboard_keyname_question);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_colon, R.string.layer_keyboard_keyname_colon);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_semicolon, R.string.layer_keyboard_keyname_semicolon);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_single_quote, R.string.layer_keyboard_keyname_single_quote);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_double_quote, R.string.layer_keyboard_keyname_double_quote);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_back_quote, R.string.layer_keyboard_keyname_back_quote);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_equal, R.string.layer_keyboard_keyname_equal);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_rmb, R.string.layer_keyboard_keyname_rmb);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_backslash, R.string.layer_keyboard_keyname_backslash);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_bar, R.string.layer_keyboard_keyname_bar);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_underline, R.string.layer_keyboard_keyname_underline);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_dollar, R.string.layer_keyboard_keyname_dollar);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_at, R.string.layer_keyboard_keyname_at);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_pound, R.string.layer_keyboard_keyname_pound);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_percent, R.string.layer_keyboard_keyname_percent);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_and, R.string.layer_keyboard_keyname_and);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_caret, R.string.layer_keyboard_keyname_caret);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_tilde, R.string.layer_keyboard_keyname_tilde);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_ellipsis, R.string.layer_keyboard_keyname_ellipsis);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_left_braces, R.string.layer_keyboard_keyname_left_braces);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_right_braces, R.string.layer_keyboard_keyname_right_braces);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_left_brackets, R.string.layer_keyboard_keyname_left_brackets);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_right_brackets, R.string.layer_keyboard_keyname_right_brackets);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_left_parentheses, R.string.layer_keyboard_keyname_left_parentheses);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_right_parentheses, R.string.layer_keyboard_keyname_right_parentheses);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_less_than, R.string.layer_keyboard_keyname_less_than);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_greater_than, R.string.layer_keyboard_keyname_greater_than);

        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_space, R.string.layer_keyboard_keyname_space);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_line_feed, R.string.layer_keyboard_keyname_line_feed);

        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_hide, R.string.layer_keyboard_keyname_hide);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_enter, R.string.layer_keyboard_keyname_enter);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_caps, R.string.layer_keyboard_keyname_caps);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_del, R.string.layer_keyboard_keyname_del);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_input_type_letter, R.string.layer_keyboard_keyname_input_type_latter);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_input_type_number, R.string.layer_keyboard_keyname_input_type_number);
        KEYCODE_MAP.put(R.id.layer_keyboard_keycode_input_type_symbol, R.string.layer_keyboard_keyname_input_type_symbol);
    }

    @Nullable
    public static String getKeyText(
            @NonNull Context context,
            @IdRes int keyCode,
            boolean isCaps
    ) {
        Integer keyName = KEYCODE_MAP.get(keyCode);
        if (keyName == null) return null;
        try {
            String keyText = context.getString(keyName);
            if (KeyCodes.isLetter(keyCode)) {
                if (isCaps) {
                    return keyText.toUpperCase();
                } else {
                    return keyText.toLowerCase();
                }
            }
            return keyText;
        } catch (Exception e) {
            return null;
        }
    }
}
