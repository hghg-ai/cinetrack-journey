Val ko thể gán lại giá trị sau khởi tạo lần đâu
Var có thể gán lại giá trị mới nhiều lần
Nên dùng val vì nếu 2 thread cùng truy cập và đổi một biến var thì dễ bị lỗi bất đồng bộ, 
dùng val loại bỏ hoàn toàn dc nguy cơ này

?. và ?: đều là công cụ xử lý Null,nhưng
?. dùng để truy cập an toàn bằng cách kiểm tra giá trị có hợp lệ hay ko,
nếu hợp lệ thì thực thi, ko thì null
val name: String? = getName()
name?.let{
println("Tên này có ${it.name} ký tự")
}
?: dùng để cung cấp giá trị thay thế cho biểu thức A,
nếu A là null thì trả về B, ko thì trả về A
val name: String? = getName()
val displayName: String = name?:"jljlj"

"!!" nguy hiểm vì nó có thể ép Kotlin coi biến có thể null thành null, gây crash chương trình
