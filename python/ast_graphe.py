import matplotlib.pyplot as plt
import sys
import json
import networkx as nx


def build_graph(node, graph, path, labels):
    if isinstance(node, dict):
        for key, value in node.items():
            child_path = f"{path}.{key}"
            graph.add_edge(path, child_path)
            labels[child_path] = key
            build_graph(value, graph, child_path, labels)
    elif isinstance(node, list):
        for i, item in enumerate(node):
            child_path = f"{path}[{i}]"
            graph.add_edge(path, child_path)
            labels[child_path] = str(i)
            build_graph(item, graph, child_path, labels)


def main():
    if len(sys.argv) > 1:
        json_path = sys.argv[1]
        with open(json_path, 'r') as f:
            json_str = f.read()
        try:
            data = json.loads(json_str)
            G = nx.DiGraph()
            labels = {}
            build_graph(data, G, 'root', labels)

            pos = nx.drawing.nx_agraph.graphviz_layout(G, prog='dot')

            plt.figure(figsize=(20, 15))

            nx.draw(G, pos, labels=labels, with_labels=True, arrows=True,
                    node_size=700, font_size=10, font_weight='bold',
                    connectionstyle='arc3,rad=0.1')
            plt.show()
        except json.JSONDecodeError:
            print("Invalid JSON")
    else:
        print("No JSON argument provided")

    with open('out.json', 'w') as f:
        f.write(json_str)


if __name__ == "__main__":
    main()
