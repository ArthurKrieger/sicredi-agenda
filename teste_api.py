import subprocess
import requests
import json
import time

def get_node_port():
    result = subprocess.run(
        ["kubectl", "get", "svc", "agenda", "-o", "json"],
        capture_output=True, text=True
    )
    svc = json.loads(result.stdout)
    return svc['spec']['ports'][0]['nodePort']

NODE_PORT = get_node_port()
BASE_URL = f"http://localhost:{NODE_PORT}"
print(f"URL: {BASE_URL}")

print("\n=== TESTE 1: Health ===")
resp = requests.get(f"{BASE_URL}/actuator/health")
print(f"Health: {resp.status_code} - {resp.text}")

print("\n=== TESTE 2: Criar Agenda ===")
resp = requests.post(f"{BASE_URL}/api/v1/agendas",
                     json={"description": "Teste com logs"})
print(f"Status: {resp.status_code}")
print(f"Response: {resp.text}")
agenda_id = resp.json()["id"]

print("\n=== TESTE 3: Criar Sessão ===")
resp = requests.post(f"{BASE_URL}/api/v1/agendas/{agenda_id}/sessions",
                     json={"durationSeconds": 60})
print(f"Status: {resp.status_code}")
print(f"Response: {resp.text}")
session_id = resp.json()["id"]

print("\n=== TESTE 4: Votar ===")
resp = requests.post(
    f"{BASE_URL}/api/v1/


    gendas/{agenda_id}/sessions/{session_id}/votes",
    json={"vote": "SIM", "cpf": "99988877766"}
)
print(f"Status: {resp.status_code}")
print(f"Response: {resp.text}")

print("\n=== TESTE 5: Consultar Agenda ===")
resp = requests.get(f"{BASE_URL}/api/v1/agendas/{agenda_id}")
print(f"Status: {resp.status_code}")
print(f"Response: {resp.json()}")

print("\n✅ Teste completo!")
print("\n📋 Agora veja os logs da aplicação:")
subprocess.run(["kubectl", "logs", "deployment/agenda", "--tail=20"])