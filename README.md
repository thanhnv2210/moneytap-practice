# moneytap-practice
# 1.Sample API request
curl --location --request POST 'http://localhost:8081/v1/hook/moengage' \
--header 'Content-Type: application/json' \
--data-raw '{
"email": "thanh@moneytap.vn",
"payload": {
"key1": "value1",
"key2": "value2"
}
}'

