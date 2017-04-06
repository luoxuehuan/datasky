package main


import "fmt"

var b string = "good"

var (
	qq int
	ww bool
)


/**
但是全局变量是允许声明但不使用。
 */

func main() {
	const CONSTA string = "abc"
	const CONSTB  = "bcd"
	var a int = 10
	fmt.Println(a)
	fmt.Println(b)
	fmt.Println(&b)//&b 的内存地址

	zzz:=34
	println(zzz)

	println(a,b)
	println(qq,ww)
	/* 这是我的第一个简单的程序 */
	fmt.Println("Hello, World!")
}