package Http.Components;
// 一个是把对应内容变成string
// 一个是变成字符数组(这个哪里需要用到?)
public interface Component {
    public String ToString();

    public byte[] ToBytes();
}
