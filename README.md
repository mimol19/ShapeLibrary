# ShapeLibrary
 
    curl -k -X POST https://localhost:8443/api/v1/shapes \
    -H "Content-Type: application/json" \
    -d '{"type": "RECTANGLE", "parameters": [15.0,8.0]}'

    curl -k https://localhost:8443/api/v1/shapes?type=RECTANGLE
