//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions are met:
//
//1. Redistributions of source code must retain the above copyright notice, this
//   list of conditions and the following disclaimer. 
//2. Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
//WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
//ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
//(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
//LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
//ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
//(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
//SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
//The views and conclusions contained in the software and documentation are those
//of the authors and should not be interpreted as representing official policies, 
//either expressed or implied, of the FreeBSD Project.
package de.learnlib.rajava.automata.graphs;

import de.learnlib.rajava.automata.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @param <V> vertices
 * @param <E> edges
 */
public class DirectedMultiGraph<V, E> {

    private final Map<V, Collection<E>> vertexes;

    private final Map<E, Pair<V, V>> edges;

    private final Map<Object, Map<String, Object>> annotations;

    public DirectedMultiGraph() {
        this.vertexes = new HashMap<>();
        this.edges = new HashMap<>();
        this.annotations = new HashMap<>();
    }

    public void addVertex(V v) {
        if (this.vertexes.containsKey(v)) {
            throw new IllegalArgumentException(
                    "Vertex " + v + " already exists");
        }

        this.vertexes.put(v, new ArrayList<E>());
    }

    public void addEdge(V src, E e, V dst) {
        if (this.edges.containsKey(e)) {
            throw new IllegalArgumentException(
                    "Edge " + e + " already exists");
        }

        if (!this.vertexes.containsKey(src)) {
            addVertex(src);
        }

        if (!this.vertexes.containsKey(dst)) {
            addVertex(dst);
        }

        this.edges.put(e, new Pair<V, V>(src, dst));
        this.vertexes.get(src).add(e);
    }

    public Collection<E> getEdges() {
        return this.edges.keySet();
    }

    public Collection<V> getVertexes() {
        return this.vertexes.keySet();
    }

    public Collection<Pair<E, V>> getAdjacentOut(V from) {
        if (!this.vertexes.containsKey(from)) {
            return null;
        }

        ArrayList<Pair<E, V>> ret = new ArrayList<Pair<E, V>>();
        for (E e : this.vertexes.get(from)) {
            ret.add(new Pair<E, V>(e, this.edges.get(e).getSecond()));
        }

        return ret;
    }

    public Collection<Pair<E, V>> getAdjacentIn(V to) {
        Collection<Pair<E, V>> in = new ArrayList<Pair<E, V>>();

        for (Map.Entry<E, Pair<V, V>> e : this.edges.entrySet()) {
            if (e.getValue().getSecond().equals(to)) {
                in.add(new Pair<E, V>(e.getKey(), e.getValue().getFirst()));
            }
        }

        return in;
    }

    public Pair<V, V> getAdjointvertexes(E e) {
        return this.edges.get(e);
    }

    public void annotate(Object a, String key, Object value) {
        if (!annotations.containsKey(a)) {
            annotations.put(a, new HashMap<String, Object>());
        }

        annotations.get(a).put(key, value);
    }

    public Object getAnnotation(Object a, String key) {
        if (!annotations.containsKey(a)) {
            return null;
        }

        return annotations.get(a).get(key);
    }

    public void removeEdge(E e) {
        Pair<V, V> vs = this.edges.remove(e);
        if (vs == null) {
            return;
        }

        this.vertexes.get(vs.getFirst()).remove(e);
        this.annotations.remove(e);
    }

    public void removeVertex(V v) {
        Collection<E> es = this.vertexes.remove(v);
        if (es == null) {
            return;
        }

        for (E e : es) {
            this.edges.remove(e);
            this.annotations.remove(e);
        }

        Collection<Pair<E, V>> remove = getAdjacentIn(v);
        for (Pair<E, V> p : remove) {
            this.edges.remove(p.getFirst());
            this.annotations.remove(p.getFirst());
        }
    }

    public void removeAnnotation(Object a, String key) {
        Map<String, Object> ans = this.annotations.get(a);
        if (a == null) {
            return;
        }

        ans.remove(key);
    }

}
