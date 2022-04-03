## kubernetes dashboard 활성화
```sh
# nohup kubectl proxy --port=8001 --address=192.168.56.30 --accept-hosts='^*$' > /dev/null 2>&1
```

## pod 생성
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: pod-1
spec:
  containers:
  - name: container1
    image: tmkube/p8000
    ports:
    - containerPort: 8000
  - name: container2
    image: tmkube/p8080
    ports:
    - containerPort: 8080
```    