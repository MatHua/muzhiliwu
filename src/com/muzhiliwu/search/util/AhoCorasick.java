package com.muzhiliwu.search.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

//ac自动机,用于多模匹配
public class AhoCorasick {
	public static final int SIGMA_SIZE = 18;// 字符集大小(共18个字符):0-9,a-f,u,\
	public static final int MAXNODE = 6 * 500;// 500个字啦~貌似都足够了

	// public static int tmpStep = 0;
	// public static int step = 0;

	private int sz;// 结点总数
	private int ch[][], fail[], last[];// fail为适配函数
	private String val[];// 保存关键字
	Set<String> result;// 保存查找结果

	public AhoCorasick() {
		sz = 1;// 初始时只有一个根节点
		ch = new int[MAXNODE][SIGMA_SIZE];
		fail = new int[MAXNODE];
		val = new String[MAXNODE];
		last = new int[MAXNODE];
		result = new HashSet<String>();
	}

	public AhoCorasick(int node) {
		sz = 1;// 初始时只有一个根节点
		ch = new int[node][SIGMA_SIZE];
		fail = new int[node];
		val = new String[node];
		last = new int[node];
		result = new HashSet<String>();
	}

	// \u7537\u751f,利用unicode编码建树
	private static int idx(char c) {
		// 0-9
		if (c >= '0' && c <= '9') {
			return c - '0';
		}
		// 10-15
		if (c >= 'a' && c <= 'f') {
			return c - 'a' + 10;
		}
		// 16
		if (c == 'u') {
			return 16;
		}
		// 17
		return c == '\\' ? 17 : -1;
	}

	/**
	 * 插入字符串s,附加信息为key
	 * 
	 * @param s
	 *            key的unicode编码
	 * @param key
	 *            关键词
	 */
	public void insert(char[] s, String key) {
		int u = 0;
		for (int i = 0; i < s.length; i++) {
			int c = idx(s[i]);
			if (ch[u][c] == 0) {// 结点不存在
				val[sz] = "";// 中间结点的附加信息为"",即为空
				ch[u][c] = sz++;// 新建结点
				// tmpStep++;
			}
			u = ch[u][c];
		}
		val[u] = key;// 字符串最后一个字符的附加信息为key(关键字)
	}

	public void insert(String s, String key) {
		insert(Unicode.toUnicode(s).toCharArray(), key);
	}

	/**
	 * 批量插入关键字
	 * 
	 * @param keys
	 *            关键字数组
	 * 
	 */
	public void insert(String[] keys) {
		for (String key : keys) {
			insert(key, key);
		}
	}

	// 在文本串T中找模板
	public void find(char[] T) {
		int j = 0;
		for (int i = 0; i < T.length; i++) {
			int c = idx(T[i]);
			/*
			 * while (j != 0 && ch[j][c] == 0) { j = fail[j]; step++; }
			 */
			j = ch[j][c];
			if (val[j] != null && val[j] != "") {
				print(j);
			} else if (last[j] != 0) {
				print(last[j]);
			}
			// step++;
		}
	}

	// 在文本串T中找模板
	public void find(String T) {
		find(Unicode.toUnicode(T).toCharArray());
	}

	private void print(int j) {
		if (j != 0) {
			if (val[j] != null && val[j] != "") {
				// step++;
				result.add(val[j]);
				print(last[j]);
			}
		}
	}

	public void lookStep() {
		System.out.println(result);
		System.out.println("关键词个数:" + result.size());
		// System.out.println("插入步数:" + tmpStep);
		// System.out.println("step:" + step + "\n");
	}

	public Object[] lookResult() {
		System.out.println(result.toArray());
		return result.toArray();
	}

	public void getFail() {
		Queue<Integer> q = new LinkedList<Integer>();
		fail[0] = 0;
		// 初始化队列
		for (int c = 0; c < SIGMA_SIZE; c++) {
			// tmpStep++;
			int u = ch[0][c];
			if (u != 0) {
				fail[u] = 0;
				q.offer(u);// 入队列
				last[u] = 0;
			}
		}
		// 按BFS顺序计算失配函数
		while (q.size() > 0) {
			// System.out.println(q.size());
			int r = q.poll();// 返回第一个元素,并在队列中删除
			for (int c = 0; c < SIGMA_SIZE; c++) {
				int u = ch[r][c];
				if (u == 0) {
					ch[r][c] = ch[fail[r]][c];
					continue;
				}
				q.offer(u);
				int v = fail[r];
				while (v != 0 && ch[v][c] == 0) {
					v = fail[v];
					// tmpStep++;
				}
				fail[u] = ch[v][c];
				last[u] = val[fail[u]] != "" ? fail[u] : last[fail[u]];
			}
		}
	}

	public static void main(String[] args) {
		AhoCorasick ac = new AhoCorasick();
		// String[] keys = { "男生", "女生", "运动型", "可爱型", "闷骚型", "天秤座", "处女座",
		// "射手座",
		// "双鱼座", "狮子座", "巨蟹座", "摩羯座", "双子座", "金牛座", "水瓶座", "天蝎座", "白羊座",
		// "20岁", "30岁", "情人节" };
		for (int i = 0; i < 1000; i++) {
			ac.insert(String.valueOf(i).toCharArray(), "关键词" + i);
		}
		// ac.insert(keys);
		ac.getFail();
		ac.find("122331898391743817812781738371".toCharArray());
		System.out.println("122331898391743817812781738371".length());
		// ac.find("20岁双鱼座运动型男生情人节");
		ac.lookStep();
	}
}
