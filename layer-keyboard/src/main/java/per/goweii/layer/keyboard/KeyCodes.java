package per.goweii.layer.keyboard;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public class KeyCodes {
    /**
     * 软键盘输入模式：字母
     */
    @IdRes
    public static final int KEYCODE_INPUT_TYPE_LETTER = R.id.layer_keyboard_keycode_input_type_letter;
    /**
     * 软键盘输入模式：数字
     */
    @IdRes
    public static final int KEYCODE_INPUT_TYPE_NUMBER = R.id.layer_keyboard_keycode_input_type_number;
    /**
     * 软键盘输入模式：符号
     */
    @IdRes
    public static final int KEYCODE_INPUT_TYPE_SYMBOL = R.id.layer_keyboard_keycode_input_type_symbol;

    /**
     * 隐藏软键盘
     */
    @IdRes
    public static final int KEYCODE_HIDE = R.id.layer_keyboard_keycode_hide;
    /**
     * 确认
     */
    @IdRes
    public static final int KEYCODE_ENTER = R.id.layer_keyboard_keycode_enter;
    /**
     * 删除
     */
    @IdRes
    public static final int KEYCODE_DEL = R.id.layer_keyboard_keycode_del;
    /**
     * 大小写切换
     */
    @IdRes
    public static final int KEYCODE_CAPS = R.id.layer_keyboard_keycode_caps;

    /**
     * 数字
     */
    @IdRes
    public static final int KEYCODE_0 = R.id.layer_keyboard_keycode_0;
    @IdRes
    public static final int KEYCODE_1 = R.id.layer_keyboard_keycode_1;
    @IdRes
    public static final int KEYCODE_2 = R.id.layer_keyboard_keycode_2;
    @IdRes
    public static final int KEYCODE_3 = R.id.layer_keyboard_keycode_3;
    @IdRes
    public static final int KEYCODE_4 = R.id.layer_keyboard_keycode_4;
    @IdRes
    public static final int KEYCODE_5 = R.id.layer_keyboard_keycode_5;
    @IdRes
    public static final int KEYCODE_6 = R.id.layer_keyboard_keycode_6;
    @IdRes
    public static final int KEYCODE_7 = R.id.layer_keyboard_keycode_7;
    @IdRes
    public static final int KEYCODE_8 = R.id.layer_keyboard_keycode_8;
    @IdRes
    public static final int KEYCODE_9 = R.id.layer_keyboard_keycode_9;

    /**
     * 字母：大小写根据{@link #KEYCODE_CAPS}判断
     */
    @IdRes
    public static final int KEYCODE_A = R.id.layer_keyboard_keycode_a;
    @IdRes
    public static final int KEYCODE_B = R.id.layer_keyboard_keycode_b;
    @IdRes
    public static final int KEYCODE_C = R.id.layer_keyboard_keycode_c;
    @IdRes
    public static final int KEYCODE_D = R.id.layer_keyboard_keycode_d;
    @IdRes
    public static final int KEYCODE_E = R.id.layer_keyboard_keycode_e;
    @IdRes
    public static final int KEYCODE_F = R.id.layer_keyboard_keycode_f;
    @IdRes
    public static final int KEYCODE_G = R.id.layer_keyboard_keycode_g;
    @IdRes
    public static final int KEYCODE_H = R.id.layer_keyboard_keycode_h;
    @IdRes
    public static final int KEYCODE_I = R.id.layer_keyboard_keycode_i;
    @IdRes
    public static final int KEYCODE_J = R.id.layer_keyboard_keycode_j;
    @IdRes
    public static final int KEYCODE_K = R.id.layer_keyboard_keycode_k;
    @IdRes
    public static final int KEYCODE_L = R.id.layer_keyboard_keycode_l;
    @IdRes
    public static final int KEYCODE_M = R.id.layer_keyboard_keycode_m;
    @IdRes
    public static final int KEYCODE_N = R.id.layer_keyboard_keycode_n;
    @IdRes
    public static final int KEYCODE_O = R.id.layer_keyboard_keycode_o;
    @IdRes
    public static final int KEYCODE_P = R.id.layer_keyboard_keycode_p;
    @IdRes
    public static final int KEYCODE_Q = R.id.layer_keyboard_keycode_q;
    @IdRes
    public static final int KEYCODE_R = R.id.layer_keyboard_keycode_r;
    @IdRes
    public static final int KEYCODE_S = R.id.layer_keyboard_keycode_s;
    @IdRes
    public static final int KEYCODE_T = R.id.layer_keyboard_keycode_t;
    @IdRes
    public static final int KEYCODE_U = R.id.layer_keyboard_keycode_u;
    @IdRes
    public static final int KEYCODE_V = R.id.layer_keyboard_keycode_v;
    @IdRes
    public static final int KEYCODE_W = R.id.layer_keyboard_keycode_w;
    @IdRes
    public static final int KEYCODE_X = R.id.layer_keyboard_keycode_x;
    @IdRes
    public static final int KEYCODE_Y = R.id.layer_keyboard_keycode_y;
    @IdRes
    public static final int KEYCODE_Z = R.id.layer_keyboard_keycode_z;

    /**
     * 空格 ' '
     */
    @IdRes
    public static final int KEYCODE_SPACE = R.id.layer_keyboard_keycode_space;
    /**
     * 换行 '\n'
     */
    @IdRes
    public static final int KEYCODE_LINE_FEED = R.id.layer_keyboard_keycode_line_feed;

    /**
     * 点号 '.'
     */
    @IdRes
    public static final int KEYCODE_DOT = R.id.layer_keyboard_keycode_dot;
    /**
     * 逗号 ','
     */
    @IdRes
    public static final int KEYCODE_COMMA = R.id.layer_keyboard_keycode_comma;
    /**
     * 加号 '+'
     */
    @IdRes
    public static final int KEYCODE_ADD = R.id.layer_keyboard_keycode_add;
    /**
     * 减号 '-'
     */
    @IdRes
    public static final int KEYCODE_MINUS = R.id.layer_keyboard_keycode_subtract;
    /**
     * 星号 '*'
     */
    @IdRes
    public static final int KEYCODE_STAR = R.id.layer_keyboard_keycode_star;
    /**
     * 斜线 '/'
     */
    @IdRes
    public static final int KEYCODE_SLASH = R.id.layer_keyboard_keycode_slash;
    /**
     * 等号 '='
     */
    @IdRes
    public static final int KEYCODE_EQUAL = R.id.layer_keyboard_keycode_equal;
    /**
     * 叹号 '!'
     */
    @IdRes
    public static final int KEYCODE_EXCLAMATION = R.id.layer_keyboard_keycode_exclamation;
    /**
     * 问号 '?'
     */
    @IdRes
    public static final int KEYCODE_QUESTION = R.id.layer_keyboard_keycode_question;
    /**
     * 冒号 ':'
     */
    @IdRes
    public static final int KEYCODE_COLON = R.id.layer_keyboard_keycode_colon;
    /**
     * 分号 ';'
     */
    @IdRes
    public static final int KEYCODE_SEMICOLON = R.id.layer_keyboard_keycode_semicolon;
    /**
     * 单引号 '''
     */
    @IdRes
    public static final int KEYCODE_SINGLE_QUOTE = R.id.layer_keyboard_keycode_single_quote;
    /**
     * 双引号 '"'
     */
    @IdRes
    public static final int KEYCODE_DOUBLE_QUOTE = R.id.layer_keyboard_keycode_double_quote;
    /**
     * 反引号 '`'
     */
    @IdRes
    public static final int KEYCODE_BACK_QUOTE = R.id.layer_keyboard_keycode_back_quote;
    /**
     * 反斜杠 '\'
     */
    @IdRes
    public static final int KEYCODE_BACKSLASH = R.id.layer_keyboard_keycode_backslash;
    /**
     * 竖线 '|'
     */
    @IdRes
    public static final int KEYCODE_BAR = R.id.layer_keyboard_keycode_bar;
    /**
     * 下划线 '_'
     */
    @IdRes
    public static final int KEYCODE_UNDERLINE = R.id.layer_keyboard_keycode_underline;
    /**
     * 美元 '$'
     */
    @IdRes
    public static final int KEYCODE_DOLLAR = R.id.layer_keyboard_keycode_dollar;
    /**
     * 人民币 '￥'
     */
    @IdRes
    public static final int KEYCODE_RMB = R.id.layer_keyboard_keycode_rmb;
    /**
     * AT '@'
     */
    @IdRes
    public static final int KEYCODE_AT = R.id.layer_keyboard_keycode_at;
    /**
     * 井号 '#'
     */
    @IdRes
    public static final int KEYCODE_POUND = R.id.layer_keyboard_keycode_pound;
    /**
     * 百分号 '%'
     */
    @IdRes
    public static final int KEYCODE_PERCENT = R.id.layer_keyboard_keycode_percent;
    /**
     * AND '&'
     */
    @IdRes
    public static final int KEYCODE_AND = R.id.layer_keyboard_keycode_and;
    /**
     * 插入 '^'
     */
    @IdRes
    public static final int KEYCODE_CARET = R.id.layer_keyboard_keycode_caret;
    /**
     * 波浪线 '~'
     */
    @IdRes
    public static final int KEYCODE_TILDE = R.id.layer_keyboard_keycode_tilde;
    /**
     * 省略号 '…'
     */
    @IdRes
    public static final int KEYCODE_ELLIPSIS = R.id.layer_keyboard_keycode_ellipsis;
    /**
     * 左大括号 '{'
     */
    @IdRes
    public static final int KEYCODE_LEFT_BRACES = R.id.layer_keyboard_keycode_left_braces;
    /**
     * 右大括号 '}'
     */
    @IdRes
    public static final int KEYCODE_RIGHT_BRACES = R.id.layer_keyboard_keycode_right_braces;
    /**
     * 左方括号 '['
     */
    @IdRes
    public static final int KEYCODE_LEFT_BRACKETS = R.id.layer_keyboard_keycode_left_brackets;
    /**
     * 右方括号 ']'
     */
    @IdRes
    public static final int KEYCODE_RIGHT_BRACKETS = R.id.layer_keyboard_keycode_right_brackets;
    /**
     * 左括号 '('
     */
    @IdRes
    public static final int KEYCODE_LEFT_PARENTHESES = R.id.layer_keyboard_keycode_left_parentheses;
    /**
     * 右括号 ')'
     */
    @IdRes
    public static final int KEYCODE_RIGHT_PARENTHESES = R.id.layer_keyboard_keycode_right_parentheses;
    /**
     * 小于号 '<'
     */
    @IdRes
    public static final int KEYCODE_LESS_THAN = R.id.layer_keyboard_keycode_less_than;
    /**
     * 大于号 '>'
     */
    @IdRes
    public static final int KEYCODE_GREATER_THAN = R.id.layer_keyboard_keycode_greater_than;

    public static final int[] KEYCODE_NUMBERS = new int[]{
            KEYCODE_0,
            KEYCODE_1,
            KEYCODE_2,
            KEYCODE_3,
            KEYCODE_4,
            KEYCODE_5,
            KEYCODE_6,
            KEYCODE_7,
            KEYCODE_8,
            KEYCODE_9
    };

    public static final int[] KEYCODE_LETTERS = new int[]{
            KEYCODE_A,
            KEYCODE_B,
            KEYCODE_C,
            KEYCODE_D,
            KEYCODE_E,
            KEYCODE_F,
            KEYCODE_G,
            KEYCODE_H,
            KEYCODE_I,
            KEYCODE_J,
            KEYCODE_K,
            KEYCODE_L,
            KEYCODE_M,
            KEYCODE_N,
            KEYCODE_O,
            KEYCODE_P,
            KEYCODE_Q,
            KEYCODE_R,
            KEYCODE_S,
            KEYCODE_T,
            KEYCODE_U,
            KEYCODE_V,
            KEYCODE_W,
            KEYCODE_X,
            KEYCODE_Y,
            KEYCODE_Z
    };

    public static final int[] KEYCODE_SPACES = new int[]{
            KEYCODE_SPACE,
            KEYCODE_LINE_FEED
    };

    public static final int[] KEYCODE_SYMBOLS = new int[]{
            KEYCODE_DOT,
            KEYCODE_COMMA,
            KEYCODE_ADD,
            KEYCODE_MINUS,
            KEYCODE_STAR,
            KEYCODE_SLASH,
            KEYCODE_EQUAL,
            KEYCODE_EXCLAMATION,
            KEYCODE_QUESTION,
            KEYCODE_COLON,
            KEYCODE_SEMICOLON,
            KEYCODE_SINGLE_QUOTE,
            KEYCODE_DOUBLE_QUOTE,
            KEYCODE_BACK_QUOTE,
            KEYCODE_BACKSLASH,
            KEYCODE_BAR,
            KEYCODE_UNDERLINE,
            KEYCODE_DOLLAR,
            KEYCODE_RMB,
            KEYCODE_AT,
            KEYCODE_POUND,
            KEYCODE_PERCENT,
            KEYCODE_AND,
            KEYCODE_CARET,
            KEYCODE_TILDE,
            KEYCODE_ELLIPSIS,
            KEYCODE_LEFT_BRACES,
            KEYCODE_RIGHT_BRACES,
            KEYCODE_LEFT_BRACKETS,
            KEYCODE_RIGHT_BRACKETS,
            KEYCODE_LEFT_PARENTHESES,
            KEYCODE_RIGHT_PARENTHESES,
            KEYCODE_LESS_THAN,
            KEYCODE_GREATER_THAN
    };

    public static boolean hasInput(@IdRes int keyCode) {
        return isLetter(keyCode) ||
                isNumber(keyCode) ||
                isSymbol(keyCode) ||
                isSpace(keyCode);
    }

    public static boolean isLetter(@IdRes int keyCode) {
        return isInArray(KEYCODE_LETTERS, keyCode);
    }

    public static boolean isNumber(@IdRes int keyCode) {
        return isInArray(KEYCODE_NUMBERS, keyCode);
    }

    public static boolean isSymbol(@IdRes int keyCode) {
        return isInArray(KEYCODE_SYMBOLS, keyCode);
    }

    public static boolean isSpace(@IdRes int keyCode) {
        return isInArray(KEYCODE_SPACES, keyCode);
    }

    private static boolean isInArray(@NonNull int[] array, int id) {
        for (int i : array) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }
}
