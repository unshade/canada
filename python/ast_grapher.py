import matplotlib.pyplot as plt
import sys
import json
import networkx as nx
import pygraphviz

def build_graph(node, graph, path):
    if isinstance(node, dict):
        for key, value in node.items():
            child_path = f"{path}.{key}"
            graph.add_edge(path, child_path)
            build_graph(value, graph, child_path)
    elif isinstance(node, list):
        for i, item in enumerate(node):
            child_path = f"{path}[{i}]"
            graph.add_edge(path, child_path)
            build_graph(item, graph, child_path)

def main():
    if len(sys.argv) > 1:
        json_str = sys.argv[1]
        try:
            data = json.loads(json_str)
            G = nx.DiGraph()
            build_graph(data, G, 'root')

            # Utiliser graphviz_layout avec le paramètre 'dot' pour une disposition de haut en bas
            pos = nx.drawing.nx_agraph.graphviz_layout(G, prog='dot')

            nx.draw(G, pos, with_labels=True, arrows=True)
            plt.show()
        except json.JSONDecodeError:
            print("Invalid JSON")
    else:
        print("No JSON argument provided")

    with open('out.json', 'w') as f:
        f.write(json_str)

if __name__ == "__main__":
    main()
